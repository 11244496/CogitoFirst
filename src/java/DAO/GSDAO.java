/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DB.ConnectionFactory;
import Entity.Annotation;
import Entity.Citizen;
import Entity.Component;
import Entity.Contractor_User;
import Entity.Employee;
import Entity.Feedback;
import Entity.Files;
import Entity.Location;
import Entity.Material;
import Entity.Meeting;
import Entity.PComments;
import Entity.PWorks;
import Entity.PlanningDocument;
import Entity.Project;
import Entity.Project_Inspection;
import Entity.Reply;
import Entity.Schedule;
import Entity.SubCategory;
import Entity.Supporter;
import Entity.TComments;
import Entity.TLocation;
import Entity.Task;
import Entity.Testimonial;
import Entity.Unit;
import Entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AdrianKyle
 */
public class GSDAO {

    Connection connection;
    PreparedStatement statement;
    ResultSet result;
    ConnectionFactory myFactory;

    public Employee getInfo(int id) {
        Employee e = null;
        User u = null;
        try {
            myFactory = ConnectionFactory.getInstance();
            connection = myFactory.getConnection();
            String query = "select * from employee join users on users.id = users_id where id = ?";

            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            result = statement.executeQuery();
            while (result.next()) {
                u = new User(result.getInt("users.id"), result.getString("username"));
                e = new Employee(result.getInt("employee.id"), result.getString("firstname"), result.getString("middlename"),
                        result.getString("lastname"), result.getString("position"), result.getString("department"), u);
            }
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAO.GSDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return e;
    }

    //================================ALL CODES ON CITITIZEN TESTIMONIALS===============================================
    //Include all parts of the testimonial
    public Testimonial getTestimonial(int id) {
        Testimonial t = new Testimonial();
        Citizen c = null;
        User u;

        ArrayList<TLocation> tlocation = new ArrayList<TLocation>();
        Project mainproject = new Project();
        ArrayList<Project> referencedproject = new ArrayList<Project>();
        ArrayList<Reply> replies = new ArrayList<Reply>();
        ArrayList<Files> files = new ArrayList<Files>();
        ArrayList<TComments> tcomments = new ArrayList<TComments>();
        ArrayList<Supporter> supporters = new ArrayList<Supporter>();

        PreparedStatement statement1, statement2, statement3, statement4, statement5, statement6, statement7, statement8;

        try {
            myFactory = ConnectionFactory.getInstance();
            connection = myFactory.getConnection();

            //Get Testimonial Info
            String testimonialquery = "select * from testimonial \n"
                    + "join citizen on citizen_id = citizen.ID\n"
                    + "join users on users_id = users.id"
                    + " where testimonial.ID = ?";

            statement = connection.prepareStatement(testimonialquery);
            statement.setInt(1, id);
            result = statement.executeQuery();
            while (result.next()) {
                u = new User(result.getInt("users.id"), result.getString("username"));
                c = new Citizen(result.getInt("citizen.id"), result.getString("FirstName"), result.getString("lastname"), u);
                t.setId(result.getInt("testimonial.id"));
                t.setTitle(result.getString("title"));
                t.setDateUploaded(result.getString("dateuploaded"));
                t.setMessage(result.getString("message"));
                t.setFolderName(result.getString("foldername"));
                t.setCategory(result.getString("category"));
                t.setStatus(result.getString("status"));
                t.setCitizen(c);
            }

            String locationQuery = ("select * from tlocation where Testimonial_ID = ?");
            statement1 = connection.prepareStatement(locationQuery);
            statement1.setInt(1, id);
            result = statement1.executeQuery();
            while (result.next()) {
                TLocation loc = new TLocation();
                loc.setId(result.getInt("ID"));
                loc.setLatitude(result.getString("latitude"));
                loc.setLongitude(result.getString("longitude"));
                Testimonial testi = new Testimonial();
                testi.setId(t.getId());
                testi.setTitle(t.getTitle());
                testi.setDateUploaded(t.getDateUploaded());
                testi.setMessage(t.getMessage());
                testi.setCategory(t.getCategory());
                testi.setFolderName(t.getFolderName());
                testi.setCitizen(t.getCitizen());
                testi.setStatus(t.getStatus());
                testi.setCitizen(c);
                loc.setTestimonial(testi);
                tlocation.add(loc);
            }
            t.setTlocation(tlocation);

            String mainprojectQuery = ("select * from project where Testimonial_ID = ?");
            statement2 = connection.prepareStatement(mainprojectQuery);
            statement2.setInt(1, id);
            result = statement2.executeQuery();
            while (result.next()) {
                mainproject.setId(result.getString("ID"));
            }
            t.setMainproject(mainproject);

            String referencedprojectQuery = ("select * from project_has_reference where Testimonial_ID = ?");
            statement7 = connection.prepareStatement(referencedprojectQuery);
            statement7.setInt(1, id);
            result = statement7.executeQuery();
            while (result.next()) {
                Project p = new Project();
                p.setId(result.getString("Project_ID"));
                referencedproject.add(p);
            }
            t.setReferencedproject(referencedproject);

            String repliesQuery = ("select * from reply where Testimonial_ID = ?");
            statement3 = connection.prepareStatement(repliesQuery);
            statement3.setInt(1, id);
            result = statement3.executeQuery();
            while (result.next()) {
                Reply r = new Reply();
                r.setId(result.getInt("ID"));
                r.setMessage(result.getString("Message"));
                User user = new User();
                user.setUsername(result.getString("Sender"));
                r.setSender(user);
                r.setDateSent(result.getString("Datesent"));
                Testimonial testi = new Testimonial();
                testi.setId(t.getId());
                testi.setTitle(t.getTitle());
                testi.setDateUploaded(t.getDateUploaded());
                testi.setMessage(t.getMessage());
                testi.setCategory(t.getCategory());
                testi.setFolderName(t.getFolderName());
                testi.setCitizen(t.getCitizen());
                testi.setStatus(t.getStatus());
                testi.setCitizen(c);
                r.setTestimonial(testi);
                replies.add(r);
            }
            t.setReplies(replies);

            String filesQuery = ("select * from files where Testimonial_ID = ?");
            statement4 = connection.prepareStatement(filesQuery);
            statement4.setInt(1, id);
            result = statement4.executeQuery();
            while (result.next()) {
                Files f = new Files();
                f.setId(result.getInt("ID"));
                f.setFileName(result.getString("FileName"));
                f.setDateUploaded(result.getString("DateUploaded"));
                f.setType(result.getString("Type"));
                f.setStatus(result.getString("Status"));
                f.setUploader(result.getString("Uploader"));
                f.setDescription(result.getString("Description"));
                Project project = new Project();
                project.setId(result.getString("Project_ID"));
                Testimonial testi = new Testimonial();
                testi.setId(t.getId());
                testi.setTitle(t.getTitle());
                testi.setDateUploaded(t.getDateUploaded());
                testi.setMessage(t.getMessage());
                testi.setCategory(t.getCategory());
                testi.setFolderName(t.getFolderName());
                testi.setCitizen(t.getCitizen());
                testi.setStatus(t.getStatus());
                testi.setCitizen(c);
                f.setTestimonial(testi);
                f.setProject(project);
                files.add(f);
            }
            t.setFiles(files);

            String commentsQuery = ("select * from tcomments where Testimonial_ID = ?");
            statement5 = connection.prepareStatement(commentsQuery);
            statement5.setInt(1, id);
            result = statement5.executeQuery();
            while (result.next()) {
                TComments comment = new TComments();
                comment.setId(result.getInt("ID"));
                comment.setComment(result.getString("Comment"));
                comment.setDateSent(result.getString("DateSent"));
                Citizen citizen = new Citizen();
                citizen.setId(result.getInt("Citizen_ID"));
                comment.setCitizen(citizen);
                Testimonial testi = new Testimonial();
                testi.setId(t.getId());
                testi.setTitle(t.getTitle());
                testi.setDateUploaded(t.getDateUploaded());
                testi.setMessage(t.getMessage());
                testi.setCategory(t.getCategory());
                testi.setFolderName(t.getFolderName());
                testi.setCitizen(t.getCitizen());
                testi.setStatus(t.getStatus());
                testi.setCitizen(c);
                comment.setTestimonial(testi);

                tcomments.add(comment);
            }
            t.setTcomments(tcomments);

            String supportersQuery = ("select * from supporters where Testimonial_ID = ?");
            statement6 = connection.prepareStatement(commentsQuery);
            statement6.setInt(1, id);
            result = statement6.executeQuery();
            while (result.next()) {
                Supporter s = new Supporter();
                s.setId(result.getInt("ID"));
                Testimonial testi = new Testimonial();
                testi.setId(t.getId());
                testi.setTitle(t.getTitle());
                testi.setDateUploaded(t.getDateUploaded());
                testi.setMessage(t.getMessage());
                testi.setCategory(t.getCategory());
                testi.setFolderName(t.getFolderName());
                testi.setCitizen(t.getCitizen());
                testi.setStatus(t.getStatus());
                testi.setCitizen(c);
                s.setTestimonial(testi);
                Citizen cit = new Citizen();
                cit.setId(result.getInt("Citizen_ID"));
                s.setCitizen(cit);

                supporters.add(s);
            }
            t.setSupporters(supporters);

            connection.close();
        } catch (SQLException ex) {
            System.out.println("Error in getTestimonial();");
            Logger.getLogger(DAO.ActivityDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return t;
    }

    public ArrayList<Testimonial> getAllTestimonials() {
        ArrayList<Testimonial> allTesti = new ArrayList<Testimonial>();
        Citizen c = null;
        User u = null;
        Testimonial t = null;
        PreparedStatement statement1;
        try {
            myFactory = ConnectionFactory.getInstance();
            connection = myFactory.getConnection();
            String testimonialquery = "select * from testimonial \n"
                    + "join citizen on citizen_id = citizen.ID\n"
                    + "join users on users_id = users.id";

            statement = connection.prepareStatement(testimonialquery);
            result = statement.executeQuery();
            while (result.next()) {
                u = new User(result.getInt("users.id"), result.getString("username"));
                c = new Citizen(result.getInt("citizen.id"), result.getString("FirstName"), result.getString("lastname"), u);
                t = new Testimonial();
                t.setId(result.getInt("testimonial.id"));
                t.setTitle(result.getString("title"));
                t.setDateUploaded(result.getString("dateuploaded"));
                t.setMessage(result.getString("message"));
                t.setFolderName(result.getString("foldername"));
                t.setCategory(result.getString("category"));
                t.setStatus(result.getString("status"));
                t.setCitizen(c);

                allTesti.add(t);
            }

            statement.close();
            connection.close();
            return allTesti;
        } catch (SQLException ex) {
            Logger.getLogger(DAO.GSDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allTesti;
    }

    public ArrayList<Testimonial> getTestimonialsNR() {
        ArrayList<Testimonial> NRtesti = new ArrayList<Testimonial>();
        Citizen c = null;
        User u = null;
        Testimonial t = null;
        try {
            myFactory = ConnectionFactory.getInstance();
            connection = myFactory.getConnection();
            String getID = "select * from testimonial join citizen on citizen_id = citizen.id join users on users.id = users_id where testimonial.id not in (select testimonial_id from reply)";
            statement = connection.prepareStatement(getID);
            result = statement.executeQuery();
            while (result.next()) {
                u = new User(result.getInt("users.id"), result.getString("username"));
                c = new Citizen();
                t = new Testimonial();
                c.setId(result.getInt("citizen.id"));
                c.setFirstName(result.getString("FirstName"));
                c.setUser(u);
                t.setId(result.getInt("testimonial.id"));
                t.setTitle(result.getString("title"));
                t.setDateUploaded(result.getString("dateuploaded"));
                t.setMessage(result.getString("message"));
                t.setFolderName(result.getString("foldername"));
                t.setCategory(result.getString("category"));
                t.setStatus(result.getString("testimonial.status"));
                t.setCitizen(c);

                NRtesti.add(t);
            }

            statement.close();
            connection.close();
            return NRtesti;
        } catch (SQLException ex) {
            Logger.getLogger(DAO.GSDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return NRtesti;
    }

    public ArrayList<Integer> getTestimonialsWR() {

        ArrayList<Integer> test = new ArrayList<Integer>();

        try {
            myFactory = ConnectionFactory.getInstance();
            connection = myFactory.getConnection();

            String query = "select distinct(testimonial.id) from testimonial join reply on testimonial_id = testimonial.id";

            statement = connection.prepareStatement(query);
            result = statement.executeQuery();

            while (result.next()) {
                int id = result.getInt("testimonial.id");
                test.add(id);
            }
            connection.close();
            return test;

        } catch (SQLException ex) {
            Logger.getLogger(CitizenDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return test;
    }

    public ArrayList<Integer> getLinkedTestimonials() {
        ArrayList<Integer> test = new ArrayList<Integer>();

        try {
            myFactory = ConnectionFactory.getInstance();
            connection = myFactory.getConnection();

            String query = "select * from project join testimonial on project.Testimonial_ID = testimonial.id";

            statement = connection.prepareStatement(query);
            result = statement.executeQuery();

            while (result.next()) {
                int id = result.getInt("testimonial.id");
                test.add(id);
            }
            connection.close();
            return test;

        } catch (SQLException ex) {
            Logger.getLogger(CitizenDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return test;
    }

    public Project getMainProjectOnTestimonial(int id) {
        Project mainproject = new Project();
        try {
            myFactory = ConnectionFactory.getInstance();
            connection = myFactory.getConnection();
            String mainprojectQuery = ("select * from project where Testimonial_ID = ?");
            statement = connection.prepareStatement(mainprojectQuery);
            statement.setInt(1, id);
            result = statement.executeQuery();
            while (result.next()) {
                mainproject.setId(result.getString("ID"));
            }

            statement.close();
            connection.close();
            return mainproject;
        } catch (SQLException ex) {
            Logger.getLogger(DAO.GSDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mainproject;
    }

    public ArrayList<Project> getReferenceProjectOnTestimonial(int id) {
        ArrayList<Project> referencedproject = new ArrayList<Project>();
        try {
            myFactory = ConnectionFactory.getInstance();
            connection = myFactory.getConnection();
            String referencedprojectQuery = ("select * from project_has_reference where Testimonial_ID = ?");
            statement = connection.prepareStatement(referencedprojectQuery);
            statement.setInt(1, id);
            result = statement.executeQuery();
            while (result.next()) {
                Project p = new Project();
                p.setId(result.getString("otherProject_ID"));
                referencedproject.add(p);
            }

            statement.close();
            connection.close();
            return referencedproject;
        } catch (SQLException ex) {
            Logger.getLogger(DAO.GSDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return referencedproject;
    }

    public void changeTestiStatus(Testimonial t) {
        try {
            myFactory = ConnectionFactory.getInstance();
            connection = myFactory.getConnection();
            String query = "update testimonial set status = ? where id = ?;";
            statement = connection.prepareStatement(query);
            statement.setString(1, t.getStatus());
            statement.setInt(2, t.getId());
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAO.GSDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean hasNoReply(Testimonial t) {
        boolean b = true;
        try {
            myFactory = ConnectionFactory.getInstance();
            connection = myFactory.getConnection();
            String query = "select count(*) c from reply where testimonial_id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, t.getId());
            result = statement.executeQuery();
            while (result.next()) {
                if (result.getInt("c")>0){
                    b = false;
                }
            }
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAO.GSDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return b;
    }

    //================================ALL CODES ON PROJECT PROPOSALS===============================================
    public void createNewProject(Project p) {
        try {
            myFactory = ConnectionFactory.getInstance();
            connection = myFactory.getConnection();
            // insert basic details
            insertProjectDetails(p);
            // get project details + id
            changeTestStatus(p.getMainTestimonial(), "Linked");
            
            for (Location l : p.getLocation()){
                insertLocationDetails(l, p);
            }
            // insert locations
            // get works from jsp check if it is existing if not, add to pworks table
            // insert pworks into project has pworks
            // assign pwork in components
            // insert components 
            // set main testimonial
            // insert referenced testimonial/s
            // insert referenced project/s

            connection.close();

        } catch (SQLException ex) {
            Logger.getLogger(DAO.GSDAO.class.getName()).log(Level.SEVERE, "Error in inserting project details", ex);
        }

        //call all methods
    }

    public void insertProjectDetails(Project p) {
        try {
            String insertProjectDetails = "insert into project (id, name, description, status, foldername, datesubmitted, category, employee_id, testimonial_id) values (?,?,?,?,?,now(),?,?,?)";
            statement = connection.prepareStatement(insertProjectDetails);
            statement.setString(1, p.getId());
            statement.setString(2, p.getName());
            statement.setString(3, p.getDescription());
            statement.setString(4, p.getStatus());
            statement.setString(5, p.getFoldername());
            statement.setString(6, p.getCategory());
            statement.setInt(7, p.getEmployee().getId());
            statement.setInt(8, p.getMainTestimonial().getId());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAO.GSDAO.class.getName()).log(Level.SEVERE, "Error in inserting project details", ex);
        }
    }

    public void changeTestStatus(Testimonial t, String status) {
        try {
            String insertProjectDetails = "update testimonial set status = ? where id = ?";
            statement = connection.prepareStatement(insertProjectDetails);
            statement.setString(1, status);
            statement.setInt(2, t.getId());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAO.GSDAO.class.getName()).log(Level.SEVERE, "Error in inserting project details", ex);
        }
    }

    public void insertLocationDetails(Location l, Project p) {
        try {
            String insertLocationDetails = "insert into location (longitude, latitude, project_id) values (?,?,?)";
            statement = connection.prepareStatement(insertLocationDetails);
            statement.setString(1, l.getLongs());
            statement.setString(2, l.getLats());
            statement.setString(3, p.getId());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAO.GSDAO.class.getName()).log(Level.SEVERE, "Error in inserting location details", ex);
        }
    }

    public ArrayList<PWorks> getExistingPWorks() {
        ArrayList<PWorks> pList = new ArrayList<>();
        PWorks p = null;
        try {
            String query = "select * from pworks";
            statement = connection.prepareStatement(query);
            result = statement.executeQuery();
            while (result.next()) {
                p = new PWorks(result.getInt("ID"), result.getString("Name"));
                pList.add(p);
            }
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAO.GSDAO.class.getName()).log(Level.SEVERE, "Error in getting existing program works", ex);
        }
        return pList;
    }

    public void insertNewPWorks(PWorks pw) {
        try {
            myFactory = ConnectionFactory.getInstance();
            connection = myFactory.getConnection();
            String query = "insert into pworks (name) values (?)";
            statement = connection.prepareStatement(query);
            statement.setString(1, pw.getName());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAO.GSDAO.class.getName()).log(Level.SEVERE, "Error in inserting new program works", ex);
        }
    }

    public void insertComponents(Component c) {
        try {
            myFactory = ConnectionFactory.getInstance();
            connection = myFactory.getConnection();
            String query = "insert into components (name, unitprice, quantity, unit_id, pworks_id) values (?,?,?,?,?)";
            statement = connection.prepareStatement(query);
            statement.setString(1, c.getName());
            statement.setFloat(2, c.getUnitPrice());
            statement.setInt(3, c.getQuantity());
            statement.setInt(4, c.getUnit().getId());
            statement.setInt(5, c.getPworks().getId());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAO.GSDAO.class.getName()).log(Level.SEVERE, "Error in inserting components", ex);
        }
    }

    public void insertUnit(Unit u) {
        try {
            myFactory = ConnectionFactory.getInstance();
            connection = myFactory.getConnection();
            String query = "insert into unit (name) values (?)";
            statement = connection.prepareStatement(query);
            statement.setString(1, u.getUnit());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAO.GSDAO.class.getName()).log(Level.SEVERE, "Error in inserting components", ex);
        }
    }

    public ArrayList<Unit> getAllUnits() {
        ArrayList<Unit> uList = new ArrayList<>();
        Unit u = null;
        try {
            myFactory = ConnectionFactory.getInstance();
            connection = myFactory.getConnection();
            String query = "select * from unit";
            statement = connection.prepareStatement(query);
            result = statement.executeQuery();
            while (result.next()) {
                u = new Unit();
                u.setId(result.getInt("ID"));
                u.setUnit(result.getString("Unit"));
                uList.add(u);
            }
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAO.GSDAO.class.getName()).log(Level.SEVERE, "Error in inserting new program works", ex);
        }
        return uList;
    }

    public void insertReferencedTestimonials(Testimonial ref, Project main) {
        try {
            myFactory = ConnectionFactory.getInstance();
            connection = myFactory.getConnection();
            String query = "insert into project_has_reference (testimonial_id, project_id) values (?,?)";
            statement = connection.prepareStatement(query);
            statement.setInt(1, ref.getId());
            statement.setString(2, main.getId());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAO.GSDAO.class.getName()).log(Level.SEVERE, "Error in inserting new program works", ex);
        }
    }

    public void insertReferencedProjects(Project ref, Project main) {
        try {
            myFactory = ConnectionFactory.getInstance();
            connection = myFactory.getConnection();
            String query = "insert into project_has_reference (otherProject_ID, project_id) values (?,?)";
            statement = connection.prepareStatement(query);
            statement.setString(1, ref.getId());
            statement.setString(2, main.getId());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAO.GSDAO.class.getName()).log(Level.SEVERE, "Error in inserting referenced projects", ex);
        }
    }

    public void insertWorksPerProject(PWorks pw, Project p) {
        try {
            myFactory = ConnectionFactory.getInstance();
            connection = myFactory.getConnection();
            String query = "insert into project_has_pworks (project_id, pworks_id) values (?,?)";
            statement = connection.prepareStatement(query);
            statement.setInt(1, pw.getId());
            statement.setString(2, p.getId());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAO.GSDAO.class.getName()).log(Level.SEVERE, "Error in inserting referenced projects", ex);
        }
    }

    public Project getBasicProjectDetails(String id) {
        Project p = new Project();
        Employee e;
        User u;

        try {
            myFactory = ConnectionFactory.getInstance();
            connection = myFactory.getConnection();

            String detailsQuery = ("select * from project join employee on employee.id = employee_id join users on users.id = users_id where project.id = ?");
            statement = connection.prepareStatement(detailsQuery);
            statement.setString(1, id);
            result = statement.executeQuery();
            while (result.next()) {
                u = new User();
                e = new Employee();
                p.setId(result.getString("project.ID"));
                p.setName(result.getString("name"));
                p.setDescription(result.getString("description"));
                p.setType(result.getString("project.type"));
                p.setStatus(result.getString("status"));
                p.setFoldername(result.getString("FolderName"));
                p.setDatesubmitted(result.getString("datesubmitted"));
                e.setId(result.getInt("employee.id"));
                u.setUsername(result.getString("username"));
                p.setEmployee(e);
                p.setCategory(result.getString("Category"));
                p.setBudget(result.getFloat("budget"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(GSDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return p;
    }
    public ArrayList<Project> getProjectsForList() {
        Project p = null;
        ArrayList<Project> pList = new ArrayList<>();
        try {
            myFactory = ConnectionFactory.getInstance();
            connection = myFactory.getConnection();

            String detailsQuery = ("select id, name, type, description, datesubmitted, status from project");
            statement = connection.prepareStatement(detailsQuery);
            result = statement.executeQuery();
            while (result.next()) {
                p = new Project();
                p.setId(result.getString("id"));
                p.setName(result.getString("name"));
                p.setDescription(result.getString("description"));
                p.setType(result.getString("project.type"));
                p.setStatus(result.getString("status"));
                p.setDatesubmitted(result.getString("datesubmitted"));
                pList.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GSDAO.class.getName()).log(Level.SEVERE, "Error in getting basic details for project list", ex);
        }
        return pList;
    }

    public ArrayList<PWorks> getWorks() {
        Collection<PWorks> wList = new ArrayList<>();
        PWorks works = null;
        try {
            myFactory = ConnectionFactory.getInstance();
            connection = myFactory.getConnection();

            String detailsQuery = "select * from pworks";
            statement = connection.prepareStatement(detailsQuery);
            result = statement.executeQuery();
            while (result.next()) {
                works = new PWorks(result.getInt("ID"), result.getString("Name"));
                wList.add(works);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GSDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList(wList);
    }

    //================================ALL CODES ON MEETINGS========================================================
    public ArrayList<Schedule> getAllMeetings(String status) {
        ArrayList<Schedule> meetingList = new ArrayList<Schedule>();
        try {
            myFactory = ConnectionFactory.getInstance();
            connection = myFactory.getConnection();
            String getID = "select * from schedule where Event = ? and Status = ?";
            statement = connection.prepareStatement(getID);
            statement.setString(1, "Meeting with OCPD");
            statement.setString(2, status);
            result = statement.executeQuery();
            while (result.next()) {
                Schedule s = new Schedule();
                s.setId(result.getInt("ID"));
                s.setEvent(result.getString("Event"));
                s.setStartdate(result.getString("StartDate"));
                s.setEnddate(result.getString("EndDate"));
                s.setStatus(result.getString("Status"));
                s.setStage(result.getString("Stage"));
                s.setDept(result.getString("Department"));
                s.setTime(result.getString("Time"));
                s.setProjectID(result.getString("Project_ID"));
                s.setRemarks(result.getString("Remarks"));
                meetingList.add(s);
            }

            statement.close();
            connection.close();
            return meetingList;
        } catch (SQLException ex) {
            Logger.getLogger(DAO.GSDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return meetingList;
    }

    public ArrayList<Task> getAgenda(Schedule s) {
        ArrayList<Task> tList = new ArrayList<>();
        Task t;
        try {
            myFactory = ConnectionFactory.getInstance();
            connection = myFactory.getConnection();
            String query = "select Description from task where schedule_id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, s.getId());
            result = statement.executeQuery();
            while (result.next()) {
                t = new Task();
                t.setDescription(result.getString("Description"));
                tList.add(t);
            }
            connection.close();
            return tList;
        } catch (SQLException ex) {
            Logger.getLogger(OCPDDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tList;
    }

    //===============================ALL CODES ON DOCUMENTS================================================
    public ArrayList<PlanningDocument> getDocuments() {
        ArrayList<PlanningDocument> documents = new ArrayList<>();
        PlanningDocument d;
        Employee e;

        try {
            myFactory = ConnectionFactory.getInstance();
            connection = myFactory.getConnection();
            String query = ("select * from planningdocument");

            statement = connection.prepareStatement(query);
            result = statement.executeQuery();

            while (result.next()) {
                d = new PlanningDocument();
                e = new Employee();
                e.setId(result.getInt("Employee_ID"));
                d.setId(result.getInt("ID"));
                d.setType(result.getString("Type"));
                d.setDateUploaded(result.getString("DateUploaded"));
                d.setUrl(result.getString("URL"));
                d.setYear(result.getInt("Year"));
                d.setName(result.getString("Name"));
                d.setEmployee(e);
                documents.add(d);
            }
            connection.close();

            return documents;

        } catch (SQLException ex) {
            Logger.getLogger(GSDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    //===============================ALL COUNT CODES=======================================================
    public int getProjectCount(String status) {
        int PPCount = 0;
        try {
            myFactory = ConnectionFactory.getInstance();
            connection = myFactory.getConnection();
            String getcount = "Select Count(*) as count from project where Status = ?";
            statement = connection.prepareStatement(getcount);
            statement.setString(1, status);
            result = statement.executeQuery();
            while (result.next()) {
                PPCount = result.getInt("count");
            }

            statement.close();
            connection.close();
            return PPCount;
        } catch (SQLException ex) {
            Logger.getLogger(DAO.GSDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return PPCount;
    }

    public void rescheduleMeeting(Schedule s) {
        try {
            myFactory = ConnectionFactory.getInstance();
            connection = myFactory.getConnection();
            String query = "update meeting set startdate = ?, enddate = ?, status = ?, time = ? where id = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, s.getStartdate());
            statement.setString(2, s.getEnddate());
            statement.setString(3, s.getStatus());
            statement.setString(4, s.getTime());
            statement.setInt(5, s.getId());
            statement.executeUpdate();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAO.GSDAO.class.getName()).log(Level.SEVERE, "Error in rescheduling meeting", ex);
        }
    }

    public void updateMeetingStatus(Schedule s, String status) {
        try {
            myFactory = ConnectionFactory.getInstance();
            connection = myFactory.getConnection();
            String query = "update meeting set status = ? where id = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, status);
            statement.setInt(2, s.getId());
            statement.executeUpdate();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAO.GSDAO.class.getName()).log(Level.SEVERE, "Error in updating meeting status", ex);
        }
    }

    public void editProposal() {

    }

    public void replyToTestimonial(Reply r) {
        try {
            myFactory = ConnectionFactory.getInstance();
            connection = myFactory.getConnection();
            String query = "insert into reply (message, sender, datesent, testimonial_id) values (?,?,now(),?)";
            statement = connection.prepareStatement(query);
            statement.setString(1, r.getMessage());
            statement.setString(2, r.getSender().getUsername());
            statement.setInt(3, r.getTestimonial().getId());
            statement.executeUpdate();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAO.GSDAO.class.getName()).log(Level.SEVERE, "Error in updating meeting status", ex);
        }
    }
    public Reply getReply(Testimonial t) {
        Reply r = null;
        Employee e;
        User u;
        try {
            myFactory = ConnectionFactory.getInstance();
            connection = myFactory.getConnection();
            String query = "select message, firstname, lastname, username, datesent from reply join users on sender = username join employee on users.id = users_id where testimonial_id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, t.getId());
            result = statement.executeQuery();
            while (result.next()){
                r = new Reply();
                r.setDateSent(result.getString("datesent"));
                r.setMessage(result.getString("message"));
                u = new User();
                e = new Employee();
                u.setUsername(result.getString("username"));
                e.setFirstName(result.getString("firstname"));
                e.setLastName(result.getString("lastname"));
                e.setUser(u);
                r.setSender(u);
            }
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAO.GSDAO.class.getName()).log(Level.SEVERE, "Error in getting reply", ex);
        }
        return r;
    }

    public Annotation getAnnotations(Project p, String status) {
        Annotation a = null;
        try {
            myFactory = ConnectionFactory.getInstance();
            connection = myFactory.getConnection();
            String query = "select * from annotation where project_id = ? and status = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, a.getProject().getId());
            statement.setString(2, status);
            result = statement.executeQuery();
            while (result.next()) {
                a = new Annotation();
                a.setId(result.getInt("ID"));
                a.setTestimonials(result.getString("Testimonials"));
                a.setProjects(result.getString("Projects"));
                a.setDetails(result.getString("Details"));
                a.setProgram(result.getString("Program"));
                a.setGeneral(result.getString("General"));
                a.setStatus(status);
                a.setDate(result.getString("Date"));
                a.setProject(p);
            }
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAO.GSDAO.class.getName()).log(Level.SEVERE, "Error in getting annotations", ex);
        }
        return a;
    }
}
