package com.javaside.views;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "configuration", layout = MainLayout.class)
@PageTitle("Configuración | Javaside")
public class ConfigurationView extends VerticalLayout {

    public ConfigurationView() {
        setSizeFull();
        setPadding(true);

        H2 title = new H2(VaadinIcon.COG.create(), new com.vaadin.flow.component.html.Span(" Configuración"));
        Paragraph description = new Paragraph("Configuración general del sistema: API keys, tokens, costos CPC/CPA, integraciones externas.");
        Paragraph status = new Paragraph("⏳ Funcionalidad en desarrollo");
        status.getStyle().set("color", "var(--lumo-secondary-text-color)");

        add(title, description, status);
    }
}
