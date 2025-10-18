package com.javaside.views;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "leads", layout = MainLayout.class)
@PageTitle("Leads | Javaside")
public class LeadsView extends VerticalLayout {

    public LeadsView() {
        setSizeFull();
        setPadding(true);

        H2 title = new H2(VaadinIcon.USERS.create(), new com.vaadin.flow.component.html.Span(" Leads"));
        Paragraph description = new Paragraph("Gestión de leads generados a través de las conversaciones con usuarios de WhatsApp y Telegram.");
        Paragraph status = new Paragraph("⏳ Funcionalidad en desarrollo");
        status.getStyle().set("color", "var(--lumo-secondary-text-color)");

        add(title, description, status);
    }
}
