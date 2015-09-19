package cellsociety_team11;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import gui.CellSocietyGUI;

public class SaxHandler extends DefaultHandler {

	private Model myModel;
	private String myNodeName;
	private Map<String, String> myAttributeMap = null;
	private List<Map<String, String>> myCells = null;

	private String currentTag = null;
	private String currentValue = null;
	
	private CellSocietyGUI myCSGUI;
	
	public SaxHandler(CellSocietyGUI CSGUI) {
		myNodeName = "model";
		myCSGUI = CSGUI;
	}

	public Model getModel() {
		return myModel;
	}

	@Override
	public void startDocument() throws SAXException {
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equals(myNodeName)) {
			myAttributeMap = new HashMap<String, String>();
		}

		if (attributes != null && myAttributeMap != null) {
			for (int i = 0; i < attributes.getLength(); i++) {
				myAttributeMap.put(attributes.getQName(i), attributes.getValue(i));
			}
		}
		currentTag = qName;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (currentTag != null && myAttributeMap != null) {
			currentValue = new String(ch, start, length);
			if (currentValue != null && !currentValue.trim().equals("") && !currentValue.trim().equals("\n")) {
				myAttributeMap.put(currentTag, currentValue);
			}
			currentTag = null;
			currentValue = null;
		}
	}

	private Model createModel(String name, int width, int height) {
		name = getClass().getPackage().getName() + "." + name;
		try {
			Class[] types = { Integer.TYPE, Integer.TYPE, CellSocietyGUI.class};
			Constructor constructor = Class.forName(name).getDeclaredConstructor(types);
			return (Model) constructor.newInstance(width, height, myCSGUI);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equals("model")) {
			String name = myAttributeMap.get("name");
			int width = Integer.parseInt(myAttributeMap.get("width"));
			int height = Integer.parseInt(myAttributeMap.get("height"));
			myModel = createModel(name, width, height);
			myModel.setParameters(myAttributeMap);
			myNodeName = "cell";
			myCells = new ArrayList<>(width * height);
			myAttributeMap = null;
		} else if (qName.equals("cell")) {
			myCells.add(myAttributeMap);
			myAttributeMap = null;
		}
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
		myModel.buildGrid(myCells, myCSGUI);
	}
}
