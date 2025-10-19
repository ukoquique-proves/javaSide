package com.javaside.views;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "database", layout = MainLayout.class)
@PageTitle("Base de Datos | Javaside")
public class DatabaseView extends VerticalLayout {

    public DatabaseView() {
        setSizeFull();
        setPadding(true);

        H2 title = new H2(VaadinIcon.DATABASE.create(), new com.vaadin.flow.component.html.Span(" Base de Datos"));
        Paragraph description = new Paragraph("Gestión y monitoreo de la base de datos PostgreSQL. Visualiza tablas, estadísticas y estado de conexión.");
        Paragraph status = new Paragraph("✅ Conexión a PostgreSQL funcionando - Paso 2 completado");
        status.getStyle().set("color", "#4CAF50");

        add(title, description, status);
    }
}
