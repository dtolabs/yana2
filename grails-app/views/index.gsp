<html>
  <head>
    <title>Welcome</title>
    <meta name="layout" content="main" />
    <style type="text/css" media="screen">

      #nav {
      margin-top:20px;
      margin-left:30px;
      width:228px;
      float:left;

      }
      .homePagePanel * {
      margin:0px;
      }
      .homePagePanel .panelBody ul {
      list-style-type:none;
      margin-bottom:10px;
      }
      .homePagePanel .panelBody h1 {
      text-transform:uppercase;
      font-size:1.1em;
      margin-bottom:10px;
      }
      .homePagePanel .panelBody {
      background: url(images/leftnav_midstretch.png) repeat-y top;
      margin:0px;
      padding:15px;
      }
      .homePagePanel .panelBtm {
      background: url(images/leftnav_btm.png) no-repeat top;
      height:20px;
      margin:0px;
      }

      .homePagePanel .panelTop {
      background: url(images/leftnav_top.png) no-repeat top;
      height:11px;
      margin:0px;
      }
      h2 {
      margin-top:15px;
      margin-bottom:15px;
      font-size:1.2em;
      }
      #pageBody {
      margin-left:280px;
      margin-right:20px;
      }
    </style>
  </head>
  <body>
    <div id="nav">
      <!--
	  <div class="homePagePanel">
	  <div class="panelTop"></div>
	  <div class="panelBody">
	  
	  </div>
	  <div class="panelBtm"></div>
	  </div>
      -->
    </div>
    <div id="pageBody">
      <!--
	  <h1>Welcome to YANA</h1>
	  <p>
	  </p>
      -->
      <div id="controllerList" class="dialog">
	<!--
	    <h2>Available Controllers:</h2>
	-->
	<ul>
	  <li>
	    <a href ="${request.contextPath}/solution">
	      Solutions
	    </a>
	  </li>
	  <li>
	    <a href ="${request.contextPath}/location">
	      Locations
	    </a>
	  </li>
	  <li>
	    <a href ="${request.contextPath}/node">
	      Nodes
	    </a>
	  </li>
	  <li>
	    <a href ="${request.contextPath}/nodetype">
	      NodeTypes
	    </a>
	  </li>
	  <li>
	    <a href ="${request.contextPath}/artifact">
	      Artifacts
	    </a>
	  </li>
	  <li>
	    <a href ="${request.contextPath}/template">
	      Templates
	    </a>
	  </li>
	  <li>
	    <a href ="${request.contextPath}/attribute">
	      Attributes
	    </a>
	  </li>
	  <li>
	    <a href ="${request.contextPath}/filter">
	      Filters
	    </a>
	  </li>
	</ul>
      </div>
    </div>
  </body>
</html>
