package com.alpi.rifugi_alpini;

import java.io.*;
import java.util.ArrayList;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


@WebServlet(name = "helloServlet", value = "/*")
public class HelloServlet extends HttpServlet {
    NodeList rowList;

    public void init() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse("./resources/Provincia-Autonoma-di-Bolzano---Elenco-dei-rifugi-alpini.xml");
            Element root = document.getDocumentElement();
            this.rowList = root.getElementsByTagName("row");
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> getRifugioValue(Element rowChilds, String[] element) {
        ArrayList<String> rifugio = new ArrayList<>();
        for (String s : element) {
            NodeList ComuniList = rowChilds.getElementsByTagName(s);
            String value = ComuniList.item(0).getFirstChild().getNodeValue();
            rifugio.add(value);
        }
        return rifugio;
    }

    private void close(HttpServletResponse response, StringBuilder sb) throws IOException {
        PrintWriter printResponse = response.getWriter();
        printResponse.write(sb.toString());
        printResponse.flush();
        printResponse.close();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //si estrae la parte terminale dell'URL
        String[] url_parts = request.getRequestURL().toString().split("/");
        String url_end = url_parts[url_parts.length - 1];

        if (url_end.equalsIgnoreCase("lista")) {
            if (rowList != null && rowList.getLength() > 0) {
                String[] dati = {"comune", "nome_italiano_rifugio", "telefono"};
                StringBuilder sb = new StringBuilder();
                sb.append("<Rifugi>\r\n");
                for (int i = 0; i < rowList.getLength(); i++) {
                    Element rowChilds = (Element) rowList.item(i);
                    ArrayList<String> rifugio = getRifugioValue(rowChilds, dati);
                    sb.append("\r\n\t<rifugio>\r\n");
                    sb.append("\t\t<comune>").append(rifugio.get(0)).append("</comune>\r\n");
                    sb.append("\t\t<nome_italiano_rifugio>").append(rifugio.get(1)).append("</nome_italiano_rifugio>\r\n");
                    sb.append("\t\t<telefono>").append(rifugio.get(2)).append("</telefono>\r\n");
                    sb.append("\t</rifugio>\r\n");
                }
                sb.append("</Rifugi>");
                response.setStatus(200);
                //invia il body della risposta
                close(response, sb);
            } else {
                response.sendError(404, "Errore Classico, ridi.");
            }
        } else if (url_end.equalsIgnoreCase("rifugio")) {
            String comune = request.getParameter("comune");
            if (comune == null){
                response.sendError(400, "Parametro sbagliato e/o dati mancanti");
            }
            if (rowList != null && rowList.getLength() > 0) {
                String[] dati = {"comune", "nome_italiano_rifugio", "telefono"};
                StringBuilder sb = new StringBuilder();
                sb.append("<Rifugi>\r\n");
                for (int i = 0; i < rowList.getLength(); i++) {
                    Element rowChilds = (Element) rowList.item(i);
                    ArrayList<String> rifugio = getRifugioValue(rowChilds, dati);
                    if (rifugio.get(0).equalsIgnoreCase(comune)) {
                        sb.append("\t<rifugio>\r\n");
                        sb.append("\t\t<comune>").append(rifugio.get(0)).append("</comune>\r\n");
                        sb.append("\t\t<nome_italiano_rifugio>").append(rifugio.get(1)).append("</nome_italiano_rifugio>\r\n");
                        sb.append("\t\t<telefono>").append(rifugio.get(2)).append("</telefono>\r\n");
                        sb.append("\t</rifugio>\r\n");
                    }
                }
                sb.append("</Rifugi>\r\n");
                response.setStatus(200);
                //invia il body della risposta
                close(response, sb);
            } else {
                response.sendError(404, "Errore Classico, ridi.");
            }
        }
    }

    public void destroy() {
    }
}
