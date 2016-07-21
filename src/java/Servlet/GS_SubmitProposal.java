/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import DAO.GSDAO;
import Entity.Component;
import Entity.Employee;
import Entity.Location;
import Entity.PWorks;
import Entity.Project;
import Entity.TLocation;
import Entity.Testimonial;
import Entity.Unit;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileUploadException;

/**
 *
 * @author RoAnn
 */
public class GS_SubmitProposal extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    GSDAO gs = new GSDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        Employee e = (Employee) session.getAttribute("user");

        try {
            Project mainProject = projectDetails(e, request);
            gs.createNewProject(mainProject);
            ServletContext context = getServletContext();

            RequestDispatcher dispatch = context.getRequestDispatcher("/GS_Home");
            dispatch.forward(request, response);

        } catch (Exception ex) {
            Logger.getLogger(GS_SubmitProposal.class.getName()).log(Level.SEVERE, "Error in submit proposal servlet", ex);
        }
    }

    private Project projectDetails(Employee e, HttpServletRequest request) {
        Project p = new Project();
        p.setId(request.getParameter("projectid"));
        p.setName(request.getParameter("projectname"));
        p.setDescription(request.getParameter("projectdescription"));
        p.setStatus("Pending");
        p.setCategory(request.getParameter("category"));
        p.setEmployee(e);
        Testimonial t = new Testimonial();
        t.setId(Integer.parseInt(request.getParameter("maintestimonial")));
        p.setMainTestimonial(t);

        String[] loc = request.getParameter("hiddenlocation").split(",");
        setProjectLocation(loc, p);

        return p;
    }

    private void setProjectLocation(String[] loc, Project p) {
        ArrayList<Location> locList = new ArrayList<>();
        ArrayList<String> longitude = new ArrayList<>();
        ArrayList<String> latitude = new ArrayList<>();

        for (int x = 0; x < loc.length; x++) {
            String[] latlong = loc[x].split("&");
            String thislat = latlong[0];
            String thislong = latlong[1];
            longitude.add(thislong);
            latitude.add(thislat);
        }

        for (int x = 0; x < loc.length; x++) {
            Location l = new Location();
            l.setLongs(longitude.get(x));
            l.setLats(latitude.get(x));
            l.setProject(p);
            locList.add(l);
        }

        p.setLocation(locList);
    }
//
//    private ArrayList<PWorks> getWorks() {
//        ArrayList<PWorks> worksList = new ArrayList<>();
//        String[] works = request.getParameterValues("works");
//        PWorks pw = null;
//
//        ArrayList<PWorks> existingWorks = gs.getExistingPWorks();
//        boolean isExisting = false;
//
//        for (String w : works) {
//            pw = new PWorks();
//            pw.setName(w);
//            for (PWorks w2 : existingWorks) {
//                if (w2.getName().equalsIgnoreCase(pw.getName())) {
//                    isExisting = true;
//                }
//            }
//
//            if (!isExisting) {
//                gs.insertNewPWorks(pw);
//                isExisting = false;
//            }
//        }
//
//        existingWorks = gs.getExistingPWorks();
//
//        for (PWorks w : existingWorks) {
//            pw = new PWorks();
//            pw.setId(w.getId());
//            pw.setName(w.getName());
//
//            for (String w2 : works) {
//                if (w2.equalsIgnoreCase(pw.getName())) {
//                    worksList.add(pw);
//                }
//            }
//        }
//        return worksList;
//    }
//
//    private void setPWorks(Project p) {
//        ArrayList<PWorks> worksList = getWorks();
//
//        for (PWorks pw : worksList) {
//            setComponentsForPOW(pw);
//        }
//
//        p.setpWorks(worksList);
//    }
//
//    private void setComponentsForPOW(PWorks p) {
//        ArrayList<Component> cList = new ArrayList<>();
//        String[] components = request.getParameterValues(p.getName() + "Components");
//        String[] qtyArea = request.getParameterValues(p.getName() + "Quantity");
//        int[] qty = new int[qtyArea.length];
//        Component c = null;
//
//        for (String s : qtyArea) {
//            int x = 0;
//            qty[x] = Integer.parseInt(s);
//            x++;
//        }
//
//        ArrayList<Unit> units = getUnits();
//        String[] unitCost = request.getParameterValues(p.getName() + "UnitCost");
//        float[] cost = new float[unitCost.length];
//
//        for (String s : unitCost) {
//            int x = 0;
//            cost[x] = Float.parseFloat(s);
//            x++;
//        }
//
//        for (int x = 0; x < components.length; x++) {
//            c = new Component();
//            c.setName(components[x]);
//            c.setQuantity(qty[x]);
//            c.setUnit(units.get(x));
//            c.setUnitPrice(cost[x]);
//            c.setPworks(p);
//            cList.add(c);
//        }
//        p.setComponents(cList);
//    }
//
//    private ArrayList<Unit> getUnits() {
//        ArrayList<Unit> existingList = gs.getAllUnits();
//        ArrayList<Unit> uList = new ArrayList<>();
//        String[] unitsFromJsp = request.getParameterValues("unit");
//        boolean isExisting = false;
//        Unit u = null;
//
//        for (String un : unitsFromJsp) {
//            u = new Unit(0, un);
//            for (Unit unit : existingList) {
//                if (u.getUnit().equalsIgnoreCase(unit.getUnit())) {
//                    isExisting = false;
//                }
//            }
//
//            if (!isExisting) {
//                gs.insertUnit(u);
//                isExisting = true;
//            }
//        }
//
//        existingList = gs.getAllUnits();
//
//        for (Unit unit : existingList) {
//            u = new Unit(unit.getId(), unit.getUnit());
//            for (String foo : unitsFromJsp) {
//                if (foo.equalsIgnoreCase(u.getUnit())) {
//                    uList.add(u);
//                }
//            }
//        }
//
//        return uList;
//    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
