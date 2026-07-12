package com.grapevine.auth.email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendUserCredentials(String to, String fullName, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Credenciales de acceso - Grapevine ERP");
        message.setText("""
                Hola %s,

                Tu cuenta ha sido creada correctamente.

                Credenciales:

                Email: %s
                Contraseña temporal: %s

                Debes cambiar tu contraseña al iniciar sesión por primera vez.

                Grapevine ERP
                """.formatted(fullName, to, password));
        mailSender.send(message);
    }

    public void sendPasswordReset(String to, String fullName, String newPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Restablecimiento de contraseña - Grapevine ERP");
        message.setText("""
                Hola %s,

                Se ha generado una nueva contraseña temporal para tu cuenta.

                Nueva contraseña temporal: %s

                Debes cambiarla al iniciar sesión.

                Si no solicitaste este cambio, contacta al administrador.

                Grapevine ERP
                """.formatted(fullName, newPassword));
        mailSender.send(message);
    }
}