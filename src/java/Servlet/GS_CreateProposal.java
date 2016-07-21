/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import DAO.CitizenDAO;
import DAO.GSDAO;
import Entity.Testimonial;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author AdrianKyle
 */
public class GS_CreateProposal extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        try {
            GSDAO gs = new GSDAO();
            CitizenDAO c = new CitizenDAO();

            //Get All Testimonials
            ArrayList<Testimonial> allTestimonials = gs.getAllTestimonials();
            for (int x = 0; x < allTestimonials.size(); x++) {
                allTestimonials.get(x).setFiles(c.getFilesWithStatus(allTestimonials.get(x).getId(),allTestimonials.get(x), "Approved"));
                allTestimonials.get(x).setMainproject(gs.getMainProjectOnTestimonial(allTestimonials.get(x).getId()));
            }

            //Request set attributes testimonials
            request.setAttribute("allTestimonials", allTestimonials);
            request.setAttribute("worksList", gs.getWorks());
            request.setAttribute("works", new Gson().toJson(gs.getWorks()));
            request.setAttribute("unitGson", new Gson().toJson(gs.getAllUnits()));
            
            ServletContext context = getServletContext();

            RequestDispatcher dispatch = context.getRequestDispatcher("/GS_CreateProposal.jsp");
            dispatch.forward(request, response);

        } finally {
            out.close();
        }
    }

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
