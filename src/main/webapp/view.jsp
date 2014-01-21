<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<script type="text/javascript" src="/XipPortlet-1.0/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="/XipPortlet-1.0/js/jquery-ui-1.9.0.custom.min.js"></script>
<script type="text/javascript" src="/XipPortlet-1.0/js/jquery.toaster.min.js"></script>

<script type="text/javascript" src="/XipPortlet-1.0/js/splitter.js"></script>
<script type="text/javascript" src="/XipPortlet-1.0/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="/XipPortlet-1.0/js/fnReloadAjax.js"></script>
<script type="text/javascript" src="/XipPortlet-1.0/js/bpopup.js"></script>
<script type="text/javascript" src="/XipPortlet-1.0/js/main.js"></script>
<link rel="stylesheet" type="text/css" href="/XipPortlet-1.0/css/jquery-ui-1.9.0.custom.css" />
<link rel="stylesheet" type="text/css" href="/XipPortlet-1.0/css/toaster_styles.css" />
<link rel="stylesheet" type="text/css" href="/XipPortlet-1.0/css/main.css" />

<div id="splitterContainer" style="height:700px">
	<div id="leftPane">
		<div id="serviceHeader">
			<span><h2>Defined Services</h2></span>
		</div>
		<div id="serviceToolbar">
		    <div>
			    <div id="addServiceGroup" style="display:inline">
			        <button id="addServiceText" class="addService">Add Service</button>
			    </div>
			    <div style="display:inline">
			    	<button id="updateService">Update</button>
			    </div>
			    <div style="display:inline">
			    	<button id="deleteService">Delete</button>
			    </div>
			    <ul id="addServiceDropdown" style="display:none">
			        <li><a href="#">Add WSDL/SOAP service</a></li>
			        <li><a href="#">Add JDBC Service</a></li>
			        <li><a href="#">Add Scripted Service</a></li>
			    </ul>
		    </div>
	    </div>
		<div id="serviceList">
			<table id="service-grid" class="grid" cellpadding="0" cellspacing="0" border="0">
				<thead>
					<tr>
						<th width="75%">Name</th>
						<th width="25%">Type</th>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
		</div>
	</div>
	<div id="rightPane">
		<div id="rightSplitterContainer" style="height:95%">
			<div id="rightTopPane">
				<div class="details-form">
					<ul class="form-two-column">
						<li>
							<label for="serviceName">Service Name</label>
							<br />
							<textarea class="one-line" id="serviceName" name="serviceName"></textarea>
						</li>
						<li>
							<label for"serviceType">Service Type</label>
							<br />
							<select id="serviceType" name="serviceType">
								<option value="wsdl">WSDL / SOAP</option>
								<option value="jdbc">JDBC</option>
								<option value="scrp">Scripted</option>
							</select>
						</li>
						<li>
							<label for="serviceProvider">Service Provider</label>
							<br />
							<textarea class="little-textarea" id="serviceProvider" name="serviceProvider"></textarea>
						</li>
						<li>
							<label for="serviceAction">Service Action</label>
							<br />
							<textarea class="one-line" id="serviceAction" name="serviceAction"></textarea>
						</li>
					</ul>
				
					<ul class="form-two-column">
						<li>
							<label for="serviceInputMappings">Input Mappings</label>
							<br />
							<textarea class="big-textarea" id="serviceInputMappings" name="serviceInputMappings"></textarea>
						</li>
						<li>
							<label for="serviceOutputMappings">Output Mappings</label>
							<br />
							<textarea class="big-textarea" id="serviceOutputMappings" name="serviceOutputMappings"></textarea>
						</li>
					</ul>
				
				</div>
				<ul class="form-one-column">
					<li>
						<label for="serviceText">Service Text</label>
						<br/>
						<textarea class="huge-textarea" id="serviceText" name="serviceText"></textarea>
					</li>
				</ul>


			</div>
			<div id="serviceLauncher">
				<span>Service URL</span><input id="serviceUrl" type="text" value="/delegate/appServices?cmd=xxx"/><input id="serviceExtraParms" type="text"/><button id="executeService">Execute</button>
			</div>
			<div id="rightBottomPane">
				<div id="serviceWrapper">
					<div id="serviceResults"></div>
				</div>
			</div>
		</div>
	</div>
</div>
