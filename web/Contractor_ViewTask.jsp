<%-- 
    Document   : GS_ViewProjectDetails
    Created on : 03 18, 16, 9:57:47 AM
    Author     : RoAnn
--%>

<%@page import="Entity.Contractor_User"%>
<%@page import="Entity.Employee"%>
<%@page import="java.time.LocalTime"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="Entity.Task"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="Entity.Material"%>
<%@page import="Entity.Testimonial"%>
<%@page import="Entity.Location"%>
<%@page import="Entity.Files"%>
<%@page import="Entity.Schedule"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Entity.Project"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%Contractor_User c = (Contractor_User) session.getAttribute("user");%>
<%
    Project p = (Project) request.getAttribute("Project");
    ArrayList<Schedule> sched = (ArrayList<Schedule>) request.getAttribute("allsched");
%>


<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="">
        <meta name="author" content="Mosaddek">
        <meta name="keyword" content="FlatLab, Dashboard, Bootstrap, Admin, Template, Theme, Responsive, Fluid, Retina">
        <link rel="shortcut icon" href="img/favicon.png">

        <title>View Project</title>

        <!-- Bootstrap core CSS -->
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/bootstrap-reset.css" rel="stylesheet">
        <!--external css-->
        <link href="assets/font-awesome/css/font-awesome.css" rel="stylesheet" >

        <!--right slidebar-->
        <link href="css/slidebars.css" rel="stylesheet">

        <!-- Custom styles for this template -->
        <link href="css/style.css" rel="stylesheet">
        <link href="css/style-responsive.css" rel="stylesheet">
        <link href = 'calendar/fullcalendar.css' rel='stylesheet'>
        <link href = 'calendar/scheduler.css' rel='stylesheet'>
        <script src ='calendar/moment.min.js'></script>
        <script src ='calendar/jquery.min.js'></script>
        <script src ='calendar/fullcalendar.js'></script>
        <script src ='calendar/scheduler.js'></script>
        <style>
            .DocumentList2
            {
                overflow-x:scroll;
                overflow-y:hidden;
                height:105%;
                white-space: nowrap;
                position: relative;
            }

            .row2 {
                width: 500%;
            }

            .DocumentItem2
            {
                border:1px solid #d9d9d9;
                margin-right: 10px;
                padding:0;
                height:200px;
                width: 200px;
            }
        </style>
        <!--<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>-->
    </head>
    <body>

        <section id="container" class="">
            <!--header start-->
            <header class="header green-bg">
                <div class="sidebar-toggle-box">
                    <div data-original-title="Toggle Navigation" data-placement="right" class="fa fa-bars tooltips"></div>
                </div>
                <!--logo start-->
                <a href="index.html" class="logo" >COGITO<span></span></a>
                <!--logo end-->
                <div class="nav notify-row" id="top_menu">
                    <!--  notification start -->
                    <ul class="nav top-menu">
                        <!-- settings start -->
                        <li class="dropdown">
                            <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                                <i class="fa fa-tasks"></i>
                                <span class="badge bg-success"><!--NUMBER OF TASKS--></span>
                            </a>
                            <ul class="dropdown-menu extended tasks-bar">
                                <div class="notify-arrow notify-arrow-green"></div>
                                <li>
                                    <p class="green">You have __ pending tasks</p>
                                </li>
                                <!--REFER TO RIGHT SIDEBAR FOR SAMPLE CODE OF TASK + PROGRESS BAR
      
                          ADD THE CODE BELOW IF AT LEAST ONE(?) TASK IS LISTED
                          
                          <li class="external">
          <a href="#">See All Tasks</a>
      </li>
                          
                                -->
                            </ul>
                        </li>
                        <!-- settings end -->
                        <!-- inbox dropdown start-->
                        <li id="header_inbox_bar" class="dropdown">
                            <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                                <i class="fa fa-envelope-o"></i>
                                <span class="badge bg-important"><!--NUMBER OF MESSAGES UNREAD--></span>
                            </a>
                            <ul class="dropdown-menu extended inbox">
                                <div class="notify-arrow notify-arrow-red"></div>
                                <li>
                                    <p class="red">You have __ new messages</p>
                                </li>
                            </ul>
                        </li>
                        <li id="header_notification_bar" class="dropdown">
                            <a data-toggle="dropdown" class="dropdown-toggle" href="#">

                                <i class="fa fa-bell-o"></i>
                                <span class="badge bg-warning"><!--NUMBER OF NOTIFICATIONS--></span>
                            </a>
                            <ul class="dropdown-menu extended notification">
                                <div class="notify-arrow notify-arrow-yellow"></div>
                                <li>
                                    <p class="yellow">You have XX notifications</p>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </div>
                <div class="top-nav ">
                    <ul class="nav pull-right top-menu">
                        <li>
                            <input type="text" class="form-control search" placeholder="">
                        </li>
                        <!-- user login dropdown start-->
                        <li class="dropdown">
                            <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                                <img alt="" src="img/avatar1_small.jpg">
                                <span class="username">Hello <b><u><%=c.getName()%></u></b>!</span>
                                <b class="caret"></b>
                            </a>
                            <ul class="dropdown-menu extended logout">
                                <div class="log-arrow-up"></div>
                                <li><a href="#"><i class=" fa fa-suitcase"></i>Profile</a></li>
                                <li><a href="#"><i class="fa fa-cog"></i> Settings</a></li>
                                <li><a href="#"><i class="fa fa-bell-o"></i> Notification</a></li>
                                <li><a href="Logout"><i class="fa fa-key"></i> Log Out</a></li>
                            </ul>
                        </li>

                        <!-- user login dropdown end -->
                        <li class="sb-toggle-right">
                            <i class="fa  fa-align-right"></i>
                        </li>
                    </ul>
                </div>
            </header>
            <!--header end-->
            <!--sidebar start-->
            <aside>
                <div id="sidebar"  class="nav-collapse ">
                    <ul class="sidebar-menu" id="nav-accordion">

                        <li>
                            <a href="Contractor_Home">
                                <i class="fa fa-dashboard"></i>
                                <span>Home</span>
                            </a>
                        </li>

                        <li class="sub-menu">
                            <a href="javascript:;" >
                                <i class="fa fa-tasks"></i>
                                <span>Bidding</span>
                            </a>
                            <ul class="sub">
                                <li><a  href="Contractor_HandleInvitations">&nbsp; &nbsp; &nbsp; &nbsp;Handle Invitations</a></li>
                                <li><a  href="Contractor_ViewHistory">&nbsp; &nbsp; &nbsp; &nbsp; View History</a></li>
                            </ul>
                        </li>

                        <li class="sub-menu">
                            <a href="javascript:;" >
                                <i class="fa fa-tasks"></i>
                                <span>Implementation</span>
                            </a>
                            <ul class="sub">
                                <li><a  href="Contractor_ViewProjectList">&nbsp; &nbsp; &nbsp; &nbsp;Monitor Projects</a></li>
                                <li><a  href="Contractor_ViewAR">&nbsp; &nbsp; &nbsp; &nbsp; View Accomplishment Reports</a></li>
                            </ul>
                        </li>

                        <li>
                            <a href="Contractor_ScheduleChange">
                                <i class="fa fa-dashboard"></i>
                                <span>Schedule Change</span>
                            </a>
                        </li>

                        <li>
                            <a href="Contractor_ActivityandNotification">
                                <i class="fa fa-dashboard"></i>
                                <span>View Activity and Notifications</span>
                            </a>
                        </li>

                    </ul>
                </div>
            </aside>
            <!--sidebar end-->
            <section id="main-content">
                <section class="wrapper site-min-height">
                    <!-- page start-->
                    <section class="panel">

                        <header class="panel-heading">
                            <h4>Project Name: <%out.print(p.getName());%></h4> 

                            <span class="pull-right">
                            </span>
                        </header>
                    </section>
                    <section class="panel">
                        <div class="row">

                            <div class="col-md-12">
                                <section class="panel">

                                    <%for (int x = 0; x < sched.size(); x++) {

                                            if (sched.get(x).getTasks().size() != 0) {
                                    %>

                                    <div class="bio-graph-heading project-heading">
                                        <strong><%out.print(sched.get(x).getEvent());%></strong>
                                        <br>
                                        <small>Status: <%out.print(sched.get(x).getStatus());%></small>
                                        <br>
                                        <small>Start Date: <%out.print(sched.get(x).getStartdate());%> - End Date: <%out.print(sched.get(x).getEnddate());%></small>
                                    </div><p>
                                    <div class="panel-body bio-graph-info">
                                        <!--<h1>New Dashboard BS3 </h1>-->
                                        <div class="row">
                                            <div class="col-lg-13" >
                                                <section class="panel">
                                                    <div class="panel-body">

                                                        <table  class="display table table-bordered table-striped" id="projectTask">
                                                            <thead id="tasktablehead">
                                                                <tr>    
                                                                    <th style='text-align: center;'>Name</th>
                                                                    <th style='text-align: center;'>Description</th>
                                                                    <th style='text-align: center;'>Start Date</th>
                                                                    <th style='text-align: center;'>End Date</th>
                                                                    <th style='text-align: center;'>Person In Charge</th>
                                                                    <th style='text-align: center;'>Status</th>
                                                                </tr>
                                                            </thead>
                                                            <%for (int y = 0; y < sched.get(x).getTasks().size(); y++) {%>

                                                            <tbody id="tasktablebody">
                                                                <tr>
                                                                    <td style='text-align: center;'><%out.print(sched.get(x).getTasks().get(y).getName());%></td>
                                                                    <td style='text-align: center;'><%out.print(sched.get(x).getTasks().get(y).getDescription());%></td>
                                                                    <td style='text-align: center;'><%out.print(sched.get(x).getTasks().get(y).getStartDate());%></td>
                                                                    <td style='text-align: center;'><%out.print(sched.get(x).getTasks().get(y).getEndDate());%></td>
                                                                    <td style='text-align: center;'><%out.print(sched.get(x).getTasks().get(y).getPersonInCharge());%></td>
                                                                    <td style='text-align: center;'><%out.print(sched.get(x).getTasks().get(y).getStatus());%></td>


                                                                </tr>
                                                            </tbody>


                                                            <%}%>
                                                        </table>
                                                    </div>


                                                </section>
                                            </div>


                                        </div>
                                    </div>

                                    <%}
                                        }%>
                                </section>

                                <center>
                                    <form action="Contractor_ViewInspectionHistory" method="POST">
                                        <input type="hidden" name="projid" value="<%=p.getId()%>">    
                                        <button type="submit" name="InspectionHistory" id="VIH">View Inspection History</button>
                                    </form>
                                </center>   

                            </div>   


                        </div>


                        <!-- page end-->
                    </section>

                </section>

            </section>
        </section>

        <!-- js placed at the end of the document so the pages load faster -->
        <!--<script src="js/jquery.js"></script>-->
        <script src="js/bootstrap.min.js"></script>
        <script class="include" type="text/javascript" src="js/jquery.dcjqaccordion.2.7.js"></script>
        <script src="js/jquery.scrollTo.min.js"></script>
        <script src="js/slidebars.min.js"></script>
        <script src="js/jquery.nicescroll.js" type="text/javascript"></script>
        <script src="js/respond.min.js" ></script>
        <script src="js/bootstrap.js"></script>
        <!--common script for all pages-->
        <script src="js/common-scripts.js"></script>


    </body>

</html>