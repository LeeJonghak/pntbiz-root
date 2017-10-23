package api.beacon.controller;

import common.JUnitTestBase;
import framework.util.JsonUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Created by nsyun on 17. 8. 18..
 */
public class BeaconExternalControllerTest extends JUnitTestBase {

	@Autowired
	private BeaconExternalController beaconExternalController;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(beaconExternalController).build();
	}

	@Test
	public void testBeaconExternalList() throws Exception {

		MvcResult mvcResult = mockMvc.perform(get("/beacon/external/706E7430-F5F8-466E-AFF9-25556B57FE6D/3"))
				.andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		Map<String, Object> resultMap = JsonUtils.readValue(response, HashMap.class);

		Assert.assertEquals(resultMap.get("result"), "success");
	}

	@Test
	public void testBeaconExternalInfo() throws Exception {

		///beacon/external/{UUID}/{majorVer}/{minorVer}/info
		MvcResult mvcResult = mockMvc.perform(get("/beacon/external/706E7430-F5F8-466E-AFF9-25556B57FE6D/60003/1234/info"))
				.andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		Map<String, Object> resultMap = JsonUtils.readValue(response, HashMap.class);

		Assert.assertEquals(resultMap.get("result"), "success");
	}

	@Test
	public void testBeaconExternalPost() throws Exception {

		//String url = "/beacon/external/{UUID}/{majorVer}/{minorVer}/{command}";
		String url = "/beacon/external/706E7430-F5F8-466E-AFF9-25556B57FE6D/60003/1234/assign";

		String content = "{\n" +
				"\t\"externalId\":\"12345\",\n" +
				"\t\"barcode\":\"6789\",\n" +
				"\t\"externalAttribute\":[\n" +
				"        {\"key\":\"name\", \"value\":\"hong\", \"displayName\":\"이름6\"},\n" +
				"        {\"key\":\"company\", \"value\":\"pntbiz\", \"displayName\":\"업체6\"}\n" +
				"\t],\n" +
				"\t\"restrictedZonePermitted\":true,\n" +
				"\t\"restrictedZone\":[\n" +
				"        {\n" +
				"            \"zoneType\":\"floor\",\n" +
				"            \"additionalAttribute\":{\n" +
				"                \"startDate\":\"2017-07-19 00:00:00\", \n" +
				"                \"endDate\":\"2017-07-19 23:59:59\"\n" +
				"            },\n" +
				"            \"zoneId\":\"2\",\n" +
				"            \"startDate\":1503563096,\n" +
				"            \"endDate\":1503563096\n" +
				"        }\n" +
				"\t]\n" +
				"}";

		MvcResult mvcResult = mockMvc.perform(post(url).content(content))
				.andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		Map<String, Object> resultMap = JsonUtils.readValue(response, HashMap.class);

		Assert.assertEquals(resultMap.get("result"), "success");
	}

	@Test
	public void testBeaconExternalLogInfo() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/beacon/external/706E7430-F5F8-466E-AFF9-25556B57FE6D/60003/1234/log"))
				.andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		Map<String, Object> resultMap = JsonUtils.readValue(response, HashMap.class);

		Assert.assertEquals(resultMap.get("result"), "success");
	}
}
