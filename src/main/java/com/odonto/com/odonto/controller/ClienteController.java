package com.odonto.com.odonto.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.odonto.com.odonto.modelos.*;
import com.odonto.com.odonto.service.UsuarioService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.odonto.com.odonto.service.UserDetailsImpl;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.util.*;

import java.security.Principal;
import java.util.List;

@Controller

//@RequestMapping("api/registros/")
@CrossOrigin("*")
public class ClienteController {
    LocalDate fechaActual = LocalDate.now();
    DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String fechaActualFormateada = fechaActual.format(formato);
    LocalDate fechaAnterior = fechaActual.minusDays(2);
    DateTimeFormatter formato2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String fechaAnteriorFormateada = fechaAnterior.format(formato2);
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public String obtenerCorreoElectronicoUsuarioActual() {
        // Obtén el objeto UserDetails del usuario actual
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername(); // Esto retorna el correo electrónico
        } else {
            // El usuario no está autenticado o los detalles no son del tipo UserDetails
            return null;
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<Cliente>> list(){
        var result = usuarioService.findAllsCliente();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    //=================================================================CLIENTES==================================================
    @GetMapping("/clientes")
    public String listarClientes(Model modelo){

        modelo.addAttribute("clientes",usuarioService.findAllClientes());
        return "clientes";

    }
    @GetMapping("/clientes2")
    public String listarClientes2(Model modelo, @RequestParam(name = "nombre") String nombre) {
        modelo.addAttribute("clientes", usuarioService.findAllClienteByNombre(nombre));
        return "clientes";
    }

    @GetMapping("/clientes/crear")
    public String mostrarCrearClientes(Model modelo){
        Cliente cliente = new Cliente();
        modelo.addAttribute("cliente",cliente);
        return "crear_clientes";
    }


    @PostMapping("/crearCliente")
    public String CrearClientes(@ModelAttribute("cliente") Cliente cliente, RedirectAttributes redirectAttributes) {
        List<Cliente> clienteExistente = usuarioService.findAllCliente(cliente.getDocumento());

        if (!clienteExistente.isEmpty()) {
            redirectAttributes.addAttribute("error2", true);
            return "redirect:/clientes/crear?error";
        }
        cliente.setCorreo("-");
        cliente.setDireccion("-");
        usuarioService.saveCliente(cliente);
        redirectAttributes.addAttribute("error3", true);
        return "redirect:/clientes/crear?exito";
    }

    @GetMapping("/clientes/modificar/{id}")
    public String mostrarModificarClientes(@PathVariable int id,Model modelo){
        List<Cliente> cliente = usuarioService.findAllCliente(id);
        Cliente clienteEncontrado = cliente.get(0);
        modelo.addAttribute("cliente",clienteEncontrado);
        return "editar_clientes";
    }
    @PostMapping("/clientes/{id}")
    public String ModificarClientes(@PathVariable int id,@ModelAttribute("cliente") Cliente cliente, Model modelo){
        List<Cliente> clienteExistente = usuarioService.findAllCliente(id);
        if (!clienteExistente.isEmpty()) {
            Cliente primerCliente = clienteExistente.get(0);
            primerCliente.setDocumento(id);
            primerCliente.setNombre(cliente.getNombre());
            primerCliente.setApellido(cliente.getApellido());
            primerCliente.setCorreo(cliente.getCorreo());
            primerCliente.setDireccion("-");
            primerCliente.setTelefono(cliente.getTelefono());


            usuarioService.updateCliente(primerCliente);
            return "redirect:/clientes";
        }

        return "redirect:/editar_clientes";
    }

    @GetMapping("/eliminarClientes/{id}")
    public String EliminarClientes(@PathVariable int id) {
        usuarioService.deleteCliente(id);
        return "redirect:/clientes";
    }
    //===================================================FACTURA===========================================

    @GetMapping("/facturas")
    public String listarFacturas(Model modelo) {
        LocalDateTime fechaActual = LocalDateTime.now();
        LocalDateTime fechaAnterior = fechaActual.minusDays(1);
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaActualFormateada = fechaActual.format(formato);
        String fechaAnteriorFormateada = fechaAnterior.format(formato);
        List<Factura> facturas = usuarioService.findAllFacturaByFecha(fechaAnteriorFormateada, fechaActualFormateada);

        // Formatea los valores de total y saldo como cadenas de texto con separadores de miles y punto decimal
        DecimalFormatSymbols separadores = new DecimalFormatSymbols(Locale.getDefault());
        separadores.setGroupingSeparator('.'); // Usa un punto como separador de miles
        separadores.setDecimalSeparator(','); // Usa una coma como separador decimal
        DecimalFormat df = new DecimalFormat("#,###.##", separadores);

        for (Factura factura : facturas) {
            if (factura.getTotal() != null) {
                factura.setTotalString(df.format(factura.getTotal()));
            }
            if (factura.getSaldo() != null) {
                factura.setSaldoString(df.format(factura.getSaldo()));
            }
        }

        modelo.addAttribute("id", "");  // Valor inicial para 'id'
        modelo.addAttribute("id2", "");
        modelo.addAttribute("facturas", facturas);

        return "facturas";
    }
    @GetMapping("/facturas/buscarPorCedula/{id}")
    public String listarFacturasPorCedula(Model modelo, @PathVariable("id") int id) {
        LocalDateTime fechaActual = LocalDateTime.now();
        LocalDateTime fechaAnterior = fechaActual.minusDays(1);
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaActualFormateada = fechaActual.format(formato);
        String fechaAnteriorFormateada = fechaAnterior.format(formato);

        modelo.addAttribute("id", "");  // Valor inicial para 'id'
        modelo.addAttribute("id2", "");
        modelo.addAttribute("facturas", usuarioService.findAllFacturasByDocumento(id));

        return "facturas";
    }


    @GetMapping("/facturas/{id}/{id2}")
    public String BuscarFacturas(@PathVariable("id") String id, @PathVariable("id2") String id2, Model modelo){

        modelo.addAttribute("facturas", usuarioService.findAllFacturaByFecha(id,id2));

        return "facturas";
    }

    @GetMapping("/facturas/crear")
    public String mostrarCrearFacturass(Model modelo){
        Factura factura = new Factura();
        modelo.addAttribute("factura", factura);
        return "crear_facturas";
    }
    @ResponseBody // Esta anotación indica que el método devuelve JSON
    @PostMapping("facturas/crear2")
    public ResponseEntity<Map<String, Double>> mostrarHistorial(@ModelAttribute("factura") Factura factura1) {
        Double saldoTotal = usuarioService.calcularSaldo(factura1.getId_cliente());

        // Crear un mapa para almacenar el saldo
        Map<String, Double> response = new HashMap<>();
        response.put("saldo", saldoTotal);

        return ResponseEntity.ok(response); // Devolver el saldo como JSON
    }
    @PostMapping("/crearFactura")
    public void CrearFacturas(@ModelAttribute("factura") Factura factura,HttpServletResponse response ) throws IOException {
        List<Cliente> documentoExistente = usuarioService.findAllCliente(factura.getId_cliente());
        List<Usuario> usuarioExistente = usuarioService.findAllUsuario(factura.getId_vendedor());

        if (!documentoExistente.isEmpty()) {
            factura.setNombreCliente(documentoExistente.get(0).getNombre());
        } else {
            // Manejar la situación en la que no se encuentra ningún cliente con el ID especificado
            // Por ejemplo, podrías redirigir al usuario a una página de error o mostrar un mensaje de advertencia
            response.sendRedirect("/facturas/crear?error23");
        }
        if (!usuarioExistente.isEmpty()) {
            factura.setNombreCliente(documentoExistente.get(0).getNombre());
        } else {
            // Manejar la situación en la que no se encuentra ningún cliente con el ID especificado
            // Por ejemplo, podrías redirigir al usuario a una página de error o mostrar un mensaje de advertencia
            response.sendRedirect("/facturas/crear?error22");
        }
        TimeZone timeZone = TimeZone.getTimeZone("America/Bogota");

        Date fechaActual = new Date();
        // Crear el formato deseado para la fecha

        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatoFecha.setTimeZone(timeZone);

        String fechaFormateada = formatoFecha.format(fechaActual);
        factura.setFecha(fechaFormateada);

        factura.setNombreVendedor(usuarioExistente.get(0).getNombre());
        //Activar estas 2 lineas si se necesita guardar el id del usuario logeado en el momento, en el documento de la factura
       // Usuario usuarioLogeado = usuarioService.findUserByEmail(obtenerCorreoElectronicoUsuarioActual());
        // factura.setId_vendedor(usuarioLogeado.getDocumento());

        usuarioService.saveFactura(factura);

        Factura factura1 = usuarioService.findAllFacturaByFecha2(fechaFormateada);
        LocalTime horaActual = LocalTime.now(timeZone.toZoneId());
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm:ss");
        String horaFormateada = horaActual.format(formato);
        Document document = new Document();

        try {
            // Configurar la respuesta HTTP para indicar que se generará un archivo PDF
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"factura.pdf\"");

            // Crear el escritor PDF y el flujo de salida
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

            // Establecer las fuentes y los estilos
            Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLACK);
            Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
            Font fontData = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

            // Abrir el documento
            document.open();
        /*    URL logoUrl = new URL("https://i.postimg.cc/XYNw6qyK/logo.jpg");
            Image logo = Image.getInstance(logoUrl);
            logo.setAlignment(Element.ALIGN_RIGHT);
            logo.scaleToFit(200, 200); // Ajusta el tamaño del logo según tus necesidades
            // Agregar el título*/

            ResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource("classpath:templates/logo.jpg");
            byte[] logoBytes = null;

            try {
                InputStream inputStream = resource.getInputStream();
                logoBytes = IOUtils.toByteArray(inputStream);
            } catch (IOException e) {
                // Manejar la excepción de lectura del recurso de imagen
            }

            Image logo = Image.getInstance(logoBytes);
            logo.scaleToFit(200, 200);
            Paragraph title = new Paragraph("Grupo Odontologico Las Palmas", fontTitle);

            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);

            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{1, 3});
            headerTable.setSpacingAfter(20);

            PdfPCell logoCell = new PdfPCell();
            logoCell.addElement(logo);
            logoCell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(logoCell);

            PdfPCell titleCell = new PdfPCell(new Paragraph("         Grupo Odontologico Las Palmas", fontTitle));
            titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            titleCell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(titleCell);

            document.add(headerTable);



            document.add(Chunk.NEWLINE);
            // Agregar los datos de la factura
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1, 3});
            table.setSpacingAfter(10);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            addCell(table, "ID:", fontHeader, BaseColor.WHITE);
            addCell(table, String.valueOf(factura1.getFactura_id()), fontData, BaseColor.WHITE);

            addCell(table, "Prospecto:", fontHeader, BaseColor.WHITE);
            addCell(table, factura1.getProspecto(), fontData, BaseColor.WHITE);

            addCell(table, "Documento:", fontHeader, BaseColor.WHITE);
            addCell(table, String.valueOf(factura1.getId_cliente()), fontData, BaseColor.WHITE);

            addCell(table, "Nombre Cliente:", fontHeader, BaseColor.WHITE);
            addCell(table, factura1.getNombreCliente(), fontData, BaseColor.WHITE);

            addCell(table, "Odontologo(a):", fontHeader, BaseColor.WHITE);
            addCell(table, factura1.getNombreVendedor(), fontData, BaseColor.WHITE);

            addCell(table, "Hora:", fontHeader, BaseColor.WHITE);
            addCell(table, String.valueOf(horaFormateada), fontData, BaseColor.WHITE);

            addCell(table, "Fecha:", fontHeader, BaseColor.WHITE);
            addCell(table, factura1.getFecha().toString(), fontData, BaseColor.WHITE);

            addCell(table, "Total:", fontHeader, BaseColor.WHITE);
            addCell(table, String.valueOf(factura1.getTotal()), fontData, BaseColor.WHITE);

            addCell(table, "Saldo:", fontHeader, BaseColor.WHITE);
            addCell(table, String.valueOf(factura1.getSaldo()), fontData, BaseColor.WHITE);

            document.add(table);
            document.add(new Paragraph("\n"));
            LineSeparator lineSeparator = new LineSeparator();
            lineSeparator.setLineColor(new BaseColor(0, 0, 0));
            lineSeparator.setLineWidth(1);
            lineSeparator.setPercentage(100);
            document.add(lineSeparator);



            document.add(new Paragraph("Comprometidos con tu SALUD ORAL"));
            document.add(new Paragraph("CITAS -3053272575 -3182821808"));
            document.add(new Paragraph("carrera 39 # 19-20 nuevo Alvernia, Tulua 763021"));

            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Firma:"));

            document.close();


            // Redirigir a "/facturas/crear?exito"


        } catch (DocumentException e) {
        } catch (IOException e) {
        }

        document.close();

    }
    private void addCell(PdfPTable table, String text, Font font, BaseColor backgroundColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
        cell.setBackgroundColor(backgroundColor);
        cell.setPadding(5);
        table.addCell(cell);
    }
    @GetMapping("/facturas/modificar/{id}")
    public String mostrarModificarFacturas(@PathVariable int id,Model modelo){
        List<Factura> factura = usuarioService.findAllFactura(id);
        Factura facturaEncontrado = factura.get(0);
        modelo.addAttribute("factura",facturaEncontrado);
        return "editar_facturas";
    }
    @PostMapping("/facturas/{id}")
    public String ModificarFacturas(@PathVariable int id,@ModelAttribute("factura") Factura factura, Model modelo){
        List<Factura> facturaExistente = usuarioService.findAllFactura(id);
        if (!facturaExistente.isEmpty()) {
            Factura primerFactura = facturaExistente.get(0);
            primerFactura.setProspecto(factura.getProspecto());
            primerFactura.setId_cliente(factura.getId_cliente());
            primerFactura.setNombreCliente(factura.getNombreCliente());
            primerFactura.setNombreVendedor(factura.getNombreVendedor());
            primerFactura.setId_vendedor(factura.getId_vendedor());
            primerFactura.setFecha(factura.getFecha());
            primerFactura.setTotal(factura.getTotal());
            primerFactura.setTotal(factura.getSaldo());


            usuarioService.updateFactura(primerFactura);
            return "redirect:/facturas";
        }

        return "redirect:/editar_facturas";
    }
    @GetMapping("/eliminarFacturas/{id}")
    public String Eliminarfacturass(@PathVariable int id) {
        usuarioService.deleteFactura(id);
        return "redirect:/facturas";
    }
    //==========================================================================USUARIOS================================0
  /*  @GetMapping("/usuarios/modificar/")
    public String mostrarModificarUsuarios(@PathVariable int id,Model modelo){
        List<Usuario> usuario = usuarioService.findAllUsuario(id);
        Usuario usuarioEncontrado = usuario.get(0);
        modelo.addAttribute("usuario",usuarioEncontrado);
        return "editar_usuarios";
    }*/
    @GetMapping("/usuarios/modificar/{id}")
    public String mostrarModificarUsuarios(@PathVariable int id,Model modelo){
        List<Usuario> usuario = usuarioService.findAllUsuario(id);
        Usuario usuarioEncontrado = usuario.get(0);
        modelo.addAttribute("usuario",usuarioEncontrado);
        return "editar_usuarios";
    }
    @PostMapping("/usuarios/{id}")
    public String ModificarUsuarios(@PathVariable int id,@ModelAttribute("usuario") Usuario usuario, Model modelo){
        String contraseñaCodificada = passwordEncoder.encode(usuario.getContraseña());

        List<Usuario> usuarioExistente = usuarioService.findAllUsuario(id);
        if (!usuarioExistente.isEmpty()) {
            Usuario primerUsuario = usuarioExistente.get(0);
            primerUsuario.setDocumento(usuario.getDocumento());
            primerUsuario.setId_vendedor(usuario.getId_vendedor());
            primerUsuario.setNombre(usuario.getNombre());
            primerUsuario.setApellido(usuario.getApellido());
            primerUsuario.setContraseña(contraseñaCodificada);
            primerUsuario.setRoles(Arrays.asList(new Rol("ROLE_USER")));


            usuarioService.updateUsuario(primerUsuario);
            return "redirect:/usuarios/modificar/"+id+"?exito";
        }

        return "redirect:/editar_usuarios";
    }
    @GetMapping("/usuarios/crear")
    public String mostrarCrearUsuarios(Model modelo, Principal principal){

        String email = principal.getName();
        Usuario usuario1 = usuarioService.findAllUsuarioByEmail(email);

        Usuario usuario = new Usuario();
        modelo.addAttribute("usuario", usuario);
        modelo.addAttribute("usuario1", usuario1);


        return "crear_usuarios";
    }
    @GetMapping("/usuarios")
    public String mostrarCrearUsuarios(Model modelo){


        modelo.addAttribute("usuario", usuarioService.findAllUsuario2s());

        return "usuarios";
    }
    @PostMapping("/crear_usuarios")
    public String CrearUsuarios(@ModelAttribute("usuario") Usuario usuario){
        String contraseñaCodificada = passwordEncoder.encode(usuario.getContraseña());

        Usuario usuario1 = new Usuario(usuario.getDocumento(),usuario.getId_vendedor(),usuario.getNombre(),usuario.getApellido()
        ,contraseñaCodificada,usuario.getCorreo());

        usuarioService.saveUsuario(usuario1);
        return "redirect:/usuarios/crear?exito";
    }
    @GetMapping("/crear_usuarios2")
    public String CrearUsuarios2(){
        Usuario usuariok1 = new Usuario(1444221,101,"admin25","esxssio","faraones22","admin6");
        String contraseñaCodificada = passwordEncoder.encode(usuariok1.getContraseña());

        Usuario usuario1 = new Usuario(usuariok1.getDocumento(),usuariok1.getId_vendedor(),usuariok1.getNombre(),usuariok1.getApellido()
                ,contraseñaCodificada,usuariok1.getCorreo());

        usuarioService.saveUsuario(usuario1);
        return "redirect:/index";
    }
    @GetMapping("/eliminarUsuarios/{id}")
    public String EliminarUsuarios(@PathVariable int id) {
        usuarioService.deleteUsuario(id);
        return "redirect:/usuarios";
    }

    /*
    public Usuario guardar(UsuarioDto usuarioDto){
        Usuario usuario = new Usuario(usuarioDto.getDocumento(),usuarioDto.getId_vendedor(),
                usuarioDto.getNombre(),usuarioDto.getApellido(),usuarioDto.getContraseña(),usuarioDto.getCorreo()
                ,Arrays.asList(new Rol("ROLE_USER")));
        return usuarioService.saveUsuario(usuario);
    }
    @PostMapping("/crearUsuario")
    public String CrearUsuarios(@ModelAttribute("usuario") Usuario usuario){
        guardar(usuario);
        return "redirect:/facturas";
    }*/
    @GetMapping
    public String mostrarFormulario(){
        return "editar_usuarios";
    }

/*
    @PostMapping("/save")
    public String save(@RequestBody Cliente cliente){

        // public ResponseEntity<ServiceResponse> save(@RequestBody Cliente cliente){
        ServiceResponse serviceResponse = new ServiceResponse();
        int result = usuarioService.saveCliente(cliente);
        if(result == 1){
            serviceResponse.setMessage("Item saved suscesfull");
        }
        return "redirect:/api/registros/clientes";
       // return new ResponseEntity<>(serviceResponse,HttpStatus.OK);
    }
    @PostMapping("/update")
    public ResponseEntity<ServiceResponse> update(@RequestBody Cliente cliente){
        ServiceResponse serviceResponse = new ServiceResponse();
        int result = usuarioService.updateCliente(cliente);
        if(result == 1){
            serviceResponse.setMessage("Item updated suscesfull");
        }
        return new ResponseEntity<>(serviceResponse,HttpStatus.OK);
    }
    @GetMapping("/delete/{id}")
    public ResponseEntity<ServiceResponse> delete(@PathVariable  int id){
        ServiceResponse serviceResponse = new ServiceResponse();
        int result = usuarioService.deleteCliente(id);
        if(result == 1){
            serviceResponse.setMessage("Item removed suscesfull");
        }
        return new ResponseEntity<>(serviceResponse,HttpStatus.OK);
    }*/
@GetMapping("/historial/modificar/{id}")
public String mostrarModificarHistorial(@PathVariable int id, Model modelo) {
    List<Historial> historial = usuarioService.findAllHistorial(id);

    Observaciones observaciones = new Observaciones();
    if (!historial.isEmpty()) {
        Historial historialEncontrado = historial.get(0);
        modelo.addAttribute("historial", historialEncontrado);
        modelo.addAttribute("observaciones1", usuarioService.findAllObservaciones(id));
        modelo.addAttribute("observaciones", observaciones);
        List<byte[]> imagenBytesList = usuarioService.obtenerImagenPorId(id);





    } else {
        LocalDateTime now = LocalDateTime.now();

        // Define un formateador para el formato deseado
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

        // Formatea la fecha y hora actual en el formato deseado
        String formattedDateTime = now.format(formatter);
        Historial historial1 = new Historial();
        historial1.setTipoIdentificacion(" ");
        historial1.setMotivoDeConsulta(" ");
        historial1.setOrigenDeLaEnfermedad(" ");
        historial1.setAntecedentesDeImportancia(" ");
        historial1.setObservaciones(" ");
        historial1.setImpresionDiagnostica(" ");
        historial1.setValoracionEspecialista(" ");
        historial1.setHistoriaDeEnfermedadActual(" ");
        historial1.setFecha(formattedDateTime);
        historial1.setIdCliente(id);
        modelo.addAttribute("historial", historial1);
        return "crear_historial";
    }

    return "crear_historial";
}

    @PostMapping("/historial/{id}")
    public String ModificarHistorial(@PathVariable int id, @ModelAttribute("historial") Historial historial, Model modelo) {
        try {
            List<Historial> historialExistente = usuarioService.findAllHistorial(id);
            if (!historialExistente.isEmpty()) {
                Historial primerHistorial = historialExistente.get(0);


                primerHistorial.setTipoIdentificacion(historial.getTipoIdentificacion());
                primerHistorial.setMotivoDeConsulta(historial.getMotivoDeConsulta());
                primerHistorial.setOrigenDeLaEnfermedad(historial.getOrigenDeLaEnfermedad());
                primerHistorial.setAntecedentesDeImportancia(historial.getAntecedentesDeImportancia());
                primerHistorial.setObservaciones(historial.getObservaciones());
                primerHistorial.setImpresionDiagnostica(historial.getImpresionDiagnostica());
                primerHistorial.setValoracionEspecialista(historial.getValoracionEspecialista());
                primerHistorial.setHistoriaDeEnfermedadActual(historial.getHistoriaDeEnfermedadActual());
                primerHistorial.setFecha(historial.getFecha());
                primerHistorial.setImagen(historial.getImagen());
                usuarioService.updateHistorial(primerHistorial);
                return "redirect:/historial/modificar/" + id ;
            } else {
                // Crear un nuevo historial
                usuarioService.saveHistorial(historial); // Suponiendo que tienes un método para guardar un nuevo historial en el servicio
                return "redirect:/historial/modificar/" + id ;
            }
        } catch (Exception e) {
            // Aquí puedes manejar la excepción, por ejemplo, registrando un mensaje de error
            // o redirigiendo a una página de error
            // También puedes lanzar una excepción personalizada si lo prefieres
            e.printStackTrace(); // Esto imprimirá la traza de la excepción en la consola
            return "redirect:/error"; // Redirige a una página de error
        }
    }
    @PostMapping("/observaciones/{id}")
    public String CrearUsuarios(@PathVariable("id") int id, @ModelAttribute("observaciones") Observaciones observaciones){
        LocalDateTime now = LocalDateTime.now();

        // Define un formateador para el formato deseado
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

        // Formatea la fecha y hora actual en el formato deseado
        String formattedDateTime = now.format(formatter);
        observaciones.setIdCliente(id);
        observaciones.setFecha(formattedDateTime);

        usuarioService.saveObservacion(observaciones);
        return "redirect:/historial/modificar/" + id ;
    }
    @GetMapping("/eliminarObservacion/{id}")
    public String EliminarObservaciones(@PathVariable int id) {
        usuarioService.deleteObservacion(id);
        return "redirect:/historial/modificar/" + id ;
    }
    @PostMapping("/crear_historial/{id}")
    public String CrearHistorial(@PathVariable int id,@ModelAttribute("historial") Historial historial){


        usuarioService.saveHistorial(historial);
        return "redirect:/historial/modificar/" + id;

}}
