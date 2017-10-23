package framework.web.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;


public class JsonUtil {

	public enum OutputType {
		DEFAULT, PRETTY
	}
	
	private OutputType outputType = OutputType.DEFAULT;
	private boolean escapeHtmlChars = false;
	private Object src;
	
	public JsonUtil(Object src) {
		this.src = src;
	}
	
	/**
	 * 출력 결과를 어떻게 보여줄지 결정한다.
	 *
	 * @see OutputType
	 *
	 * @param outputType
	 * @return
	 */
	public JsonUtil setOutputType(OutputType outputType) {
		this.outputType = outputType;
		return this;
	}
	
	public JsonUtil setEscapeHtmlChars(boolean escapeHtmlChars) {
		this.escapeHtmlChars = escapeHtmlChars;
		return this;
	}
	
	public static String toJson(Object src) throws IOException {
		return toJson(src, OutputType.DEFAULT);
	}
	
	public static String toJson(Object src, boolean escapeHtmlChars) throws IOException {
		return toJson(src, OutputType.DEFAULT, escapeHtmlChars);
	}
	
	public static String toJson(Object src, OutputType outputType) throws IOException {
		JsonUtil util = new JsonUtil(src);
		util.setOutputType(outputType);
		return util.toJson();
	}
	
	public static String toJson(Object src, OutputType outputType, boolean escapeHtmlChars) throws IOException {
		JsonUtil util = new JsonUtil(src);
		util.setOutputType(outputType);
		util.setEscapeHtmlChars(escapeHtmlChars);
		return util.toJson();
	}
	
	public String toJson() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		if (this.escapeHtmlChars) {
			mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
		}
		switch (this.outputType) {
			case PRETTY:
				mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
				break;
			default:
				break;
		}
		return mapper.writeValueAsString(src);
	}
	
	public static <T> T fromJson(String content, Class<T> valueType) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(content, valueType);
	}
	
	/**
	 * JSON String Post
	 * @param url
	 * @param method
	 * @param json
	 * @return
	 * @throws IOException
	 */
	public static String send(String url, String method, String json) throws IOException {
		URL postUrl = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
		connection.setDoOutput(true);
		connection.setInstanceFollowRedirects(false);
		connection.setRequestMethod(method);
		connection.setRequestProperty("Accept-Charset", "UTF-8");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("charset", "UTF-8");
		if(!method.toLowerCase().equals("delete")) {
			System.out.println("lower");
			OutputStream os= connection.getOutputStream();
			os.write(json.getBytes());
			os.flush();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
		String output;
		String result = "";
		while ((output = br.readLine()) != null) {
			//System.out.println(output);
			if(output != null || "".equals(output)) result += output; 
		}
		connection.disconnect();
		return result;
	}
	
	/**
	 * Request 정보에서 body 가져오기
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static String getJson(HttpServletRequest request) throws IOException {
		String json = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
 
        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }
 
        json = stringBuilder.toString();
        return json;
	}
	
	/**
	 * JSON포멧의 스트링을 노드형태로 변경
	 * @param json
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public static JsonNode toNode(String json) throws JsonProcessingException, IOException {
		ObjectMapper om = new ObjectMapper();
        JsonNode node = om.readTree(json);
        return node;
	}
	
}