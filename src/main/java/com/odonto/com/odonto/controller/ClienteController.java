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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;



import java.security.Principal;
import java.util.Arrays;
import java.util.Date;
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
            return "redirect:/clientes/crear";
        }

        usuarioService.saveCliente(cliente);
        return "redirect:/clientes";
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
            primerCliente.setDireccion(cliente.getDireccion());
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

        modelo.addAttribute("id", "");  // Valor inicial para 'id'
        modelo.addAttribute("id2", "");
        modelo.addAttribute("facturas", usuarioService.findAllFacturaByFecha(fechaAnteriorFormateada, fechaActualFormateada));

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
    @PostMapping("/crearFactura")
    public void CrearFacturas(@ModelAttribute("factura") Factura factura,HttpServletResponse response ) throws IOException {
        List<Cliente> documentoExistente = usuarioService.findAllCliente(factura.getId_cliente());

        if (documentoExistente.isEmpty()) {
            response.sendRedirect("/facturas/crear?error2");
        }
        Date fechaActual = new Date();
        // Crear el formato deseado para la fecha
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fechaFormateada = formatoFecha.format(fechaActual);
        factura.setFecha(fechaFormateada);
        usuarioService.saveFactura(factura);

        Factura factura1 = usuarioService.findAllFacturaByFecha2(fechaFormateada);
        LocalTime horaActual = LocalTime.now();
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
            Resource resource = resourceLoader.getResource("classpath:img/logo.jpg");
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
    @PostMapping("/crear_usuarios")
    public String CrearUsuarios(@ModelAttribute("usuario") Usuario usuario){
        String contraseñaCodificada = passwordEncoder.encode(usuario.getContraseña());

        Usuario usuario1 = new Usuario(usuario.getDocumento(),usuario.getId_vendedor(),usuario.getNombre(),usuario.getApellido()
        ,contraseñaCodificada,usuario.getCorreo());

        usuarioService.saveUsuario(usuario1);
        return "redirect:/usuarios/crear?exito";
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
}
