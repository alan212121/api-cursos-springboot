package com.example.demo.controller;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.SolicitudArco;
import com.example.demo.model.Usuario;
import com.example.demo.repository.SolicitudArcoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.EmailService;
import com.example.demo.util.Hash;

@RestController
@RequestMapping("/api/arco")
public class ArcoController {

    public record Credenciales(String nombre, String correo, String password) {}

    private final UsuarioRepository usuarios;
    private final SolicitudArcoRepository solicitudes;
    private final EmailService email;

    @Value("${arco.mostrar-codigo:false}")
    private boolean mostrarCodigo;

    public ArcoController(UsuarioRepository usuarios, SolicitudArcoRepository solicitudes, EmailService email) {
        this.usuarios = usuarios;
        this.solicitudes = solicitudes;
        this.email = email;
    }

    // ---------- RECTIFICACIÓN ----------

    @PostMapping("/cambio-pass")
    public ResponseEntity<Map<String, Object>> cambioPass(
            @RequestParam(defaultValue = "correo") String por,
            @RequestParam String newpass,
            @RequestBody Credenciales c) {
        Usuario u = verificarTriple(por, c);
        if (u == null) return noVerificado();
        u.passwordHash = Hash.sha256(newpass);
        usuarios.save(u);
        registrar(u, "RECTIFICACION", "Cambio de password", "COMPLETADO", null);
        return ResponseEntity.ok(msg("Password actualizado"));
    }

    @PostMapping("/cambio-name")
    public ResponseEntity<Map<String, Object>> cambioName(
            @RequestParam String newname,
            @RequestBody Credenciales c) {
        Usuario u = porCorreo(c.correo(), c.password());
        if (u == null) return noVerificado();
        u.nombre = newname;
        usuarios.save(u);
        registrar(u, "RECTIFICACION", "Cambio de nombre", "COMPLETADO", null);
        return ResponseEntity.ok(msg("Nombre actualizado"));
    }

    @PostMapping("/cambio-correo")
    public ResponseEntity<Map<String, Object>> cambioCorreo(
            @RequestParam String newcorreo,
            @RequestBody Credenciales c) {
        Usuario u = porNombre(c.nombre(), c.password());
        if (u == null) return noVerificado();
        if (usuarios.findFirstByCorreo(newcorreo).isPresent()) {
            return ResponseEntity.status(409).body(msg("Ese correo ya está en uso"));
        }
        u.correo = newcorreo;
        usuarios.save(u);
        registrar(u, "RECTIFICACION", "Cambio de correo", "COMPLETADO", null);
        return ResponseEntity.ok(msg("Correo actualizado"));
    }

    @PostMapping("/cambio-tel")
    public ResponseEntity<Map<String, Object>> cambioTel(
            @RequestParam String newtel,
            @RequestBody Credenciales c) {
        Usuario u = porCorreo(c.correo(), c.password());
        if (u == null) return noVerificado();
        u.telefono = newtel;
        usuarios.save(u);
        registrar(u, "RECTIFICACION", "Cambio de teléfono", "COMPLETADO", null);
        return ResponseEntity.ok(msg("Teléfono actualizado"));
    }

    // ---------- OLVIDÉ EL PASS ----------

    @PostMapping("/olvide-pass")
    public ResponseEntity<Map<String, Object>> olvidePass(@RequestBody Credenciales c) {
        Usuario u = usuarios.findFirstByCorreo(c.correo()).orElse(null);
        if (u == null || !Objects.equals(u.nombre, c.nombre()) || !Boolean.TRUE.equals(u.activo)) {
            return noVerificado();
        }
        String token = UUID.randomUUID().toString().substring(0, 8);
        registrar(u, "RECUPERACION", "Olvidé mi password", "PENDIENTE", token);
        boolean enviado = email.enviar(u.correo, "Token para restablecer tu password",
                "Hola " + u.nombre + ",\n\nTu token es: " + token + "\nVence en 10 minutos.");
        if (!enviado && !mostrarCodigo) {
            return ResponseEntity.status(502).body(msg("No se pudo enviar el correo"));
        }
        Map<String, Object> m = msg(enviado ? "Token enviado a tu correo" : "Correo no enviado (modo prueba)");
        if (mostrarCodigo) m.put("token", token);
        return ResponseEntity.ok(m);
    }

    @PostMapping("/olvide-pass/confirmar")
    public ResponseEntity<Map<String, Object>> confirmarOlvide(
            @RequestParam(required = false) String verificado,
            @RequestParam(required = false) String codigo,
            @RequestParam String newpass) {
        SolicitudArco s = vigente(verificado != null ? verificado : codigo, "RECUPERACION");
        if (s == null) return codigoInvalido();
        Usuario u = usuarios.findById(s.idUsuario).orElse(null);
        if (u == null) return codigoInvalido();
        u.passwordHash = Hash.sha256(newpass);
        usuarios.save(u);
        cerrar(s);
        return ResponseEntity.ok(msg("Password restablecido"));
    }

    // ---------- CANCELACIÓN ----------

    @PostMapping("/cancelar")
    public ResponseEntity<Map<String, Object>> cancelar(@RequestBody Credenciales c) {
        Usuario u = null;
        if (c.correo() != null) u = usuarios.findFirstByCorreo(c.correo()).orElse(null);
        if (u == null && c.nombre() != null) u = porNombre(c.nombre(), c.password());
        if (u == null || !Boolean.TRUE.equals(u.activo)) return noVerificado();
        String code = String.format("%06d", new SecureRandom().nextInt(1_000_000));
        registrar(u, "CANCELACION", "Solicitud de cancelación de cuenta", "PENDIENTE", code);
        boolean enviado = email.enviar(u.correo, "Code para cancelar tu cuenta",
                "Hola " + u.nombre + ",\n\nTu code es: " + code
                + "\nVence en 10 minutos.\nSi no lo pediste, ignora este mensaje.");
        if (!enviado && !mostrarCodigo) {
            return ResponseEntity.status(502).body(msg("No se pudo enviar el correo"));
        }
        Map<String, Object> m = msg(enviado ? "Code enviado a tu correo" : "Correo no enviado (modo prueba)");
        if (mostrarCodigo) m.put("codigo", code);
        return ResponseEntity.ok(m);
    }

    @PostMapping("/cancelar/confirmar")
    public ResponseEntity<Map<String, Object>> confirmarCancelar(
            @RequestParam(required = false) String verificado,
            @RequestParam(required = false) String codigo) {
        SolicitudArco s = vigente(verificado != null ? verificado : codigo, "CANCELACION");
        if (s == null) return codigoInvalido();
        Usuario u = usuarios.findById(s.idUsuario).orElse(null);
        if (u == null) return codigoInvalido();
        u.activo = false;
        usuarios.save(u);
        cerrar(s);
        return ResponseEntity.ok(msg("Cuenta cancelada"));
    }

    // ---------- UTILIDADES ----------

    private Usuario validar(Usuario u, String password) {
        if (u == null || password == null || !Boolean.TRUE.equals(u.activo)) return null;
        return Hash.sha256(password).equals(u.passwordHash) ? u : null;
    }

    private Usuario porCorreo(String correo, String password) {
        return validar(usuarios.findFirstByCorreo(correo).orElse(null), password);
    }

    private Usuario porNombre(String nombre, String password) {
        return validar(usuarios.findFirstByNombre(nombre).orElse(null), password);
    }

    private Usuario verificarTriple(String por, Credenciales c) {
        if ("name".equals(por)) {
            Usuario u = porNombre(c.nombre(), c.password());
            return (u != null && Objects.equals(u.correo, c.correo())) ? u : null;
        }
        Usuario u = porCorreo(c.correo(), c.password());
        return (u != null && Objects.equals(u.nombre, c.nombre())) ? u : null;
    }

    private SolicitudArco vigente(String codigo, String tipo) {
        if (codigo == null) return null;
        return solicitudes.findFirstByCodigoAndTipoDerechoAndEstado(codigo, tipo, "PENDIENTE")
                .filter(s -> s.expiraEn != null && s.expiraEn.isAfter(LocalDateTime.now()))
                .orElse(null);
    }

    private void cerrar(SolicitudArco s) {
        s.estado = "COMPLETADO";
        s.fechaRespuesta = LocalDateTime.now();
        s.codigo = null;
        solicitudes.save(s);
    }

    private void registrar(Usuario u, String tipo, String detalles, String estado, String codigo) {
        SolicitudArco s = new SolicitudArco();
        s.idUsuario = u.idUsuario;
        s.tipoDerecho = tipo;
        s.detalles = detalles;
        s.estado = estado;
        s.codigo = codigo;
        if (codigo != null) s.expiraEn = LocalDateTime.now().plusMinutes(10);
        if ("COMPLETADO".equals(estado)) s.fechaRespuesta = LocalDateTime.now();
        solicitudes.save(s);
    }

    private static Map<String, Object> msg(String mensaje) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("mensaje", mensaje);
        return m;
    }

    private ResponseEntity<Map<String, Object>> noVerificado() {
        return ResponseEntity.status(401).body(msg("Datos no válidos: no se pudo verificar la identidad"));
    }

    private ResponseEntity<Map<String, Object>> codigoInvalido() {
        return ResponseEntity.status(400).body(msg("Token o code inválido, expirado o ya usado"));
    }
}