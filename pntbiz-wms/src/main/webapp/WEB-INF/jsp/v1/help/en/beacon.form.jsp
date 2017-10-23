<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<h2>[Register Beacon] page</h2>

<p>
The page is for registration of a new beacon.
</p>

<h4>Register Beacon</h4>

<p>
Register of a new beacon with detail values for each setting.
</p>

<ol>
	<li>UUID<br />-	Universally Unique Identifier for the beacon. Normally UUID is provided from manufacturer.</li>
	<li>Major Version<br />-	Major Version of beacon</li>
	<li>Minor Version<br />-	Minor Version of beacon</li>
	<li>Beacon Name<br />-	Name of beacon. Owner can make any name. It does not have to be unique.</li>
	<li>TxPower<br />-	This field is reserved field.</li>
	<li>Latitude/Longitude<br />-  Click Show Map button and then can click the location of the beacon in the map with mouse. Latitude and longitude values are automatically generated according to the location. </li>
	<li>Floor<br />-	Select the floor the beacon is installed. Note that you need to create floor(s) in advance through Map->Floor Management menu.</li>
	<li>Description<br />-	Any additional comments about the beacon</li>
</ol>

<h4>Register Button</h4>

<p>
By clicking Register Button, you can execute registration of the beacon.
</p>

<h4>List Button</h4>

<p>
By clicking List Button, you can access Beacon List page.
</p>