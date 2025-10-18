package com.javaside.views;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "telegram", layout = MainLayout.class)
@PageTitle("Telegram | Javaside")
public class TelegramView extends VerticalLayout {

    public TelegramView() {
        setSizeFull();
        setPadding(true);

        H2 title = new H2(VaadinIcon.CHAT.create(), new com.vaadin.flow.component.html.Span(" Telegram"));
        Paragraph description = new Paragraph("Configuración y monitoreo del bot de Telegram. Visualiza mensajes recibidos y estadísticas de uso.");
        Paragraph status = new Paragraph("⏳ Funcionalidad en desarrollo - Paso 3");
        status.getStyle().set("color", "var(--lumo-secondary-text-color)");

        add(title, description, status);
    }
}
