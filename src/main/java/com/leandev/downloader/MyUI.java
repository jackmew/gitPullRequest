package com.leandev.downloader;

import java.io.File;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import elemental.json.JsonArray;

/**
 *
 */
@Theme("mytheme")
@Widgetset("com.leandev.downloader.MyAppWidgetset")
public class MyUI extends UI {
    // add comments	
	private VerticalLayout layout ;
	private Button downloadInvisibleButton ;
	private Button button ;
	private Button.ClickListener bc ;
    @Override
    protected void init(VaadinRequest vaadinRequest) {
		
		// start template
        layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);
        
        button = new Button("Click to download");
        button.setDisableOnClick(true);
        layout.addComponent(button);
        setButtonClickListener();
        button.addClickListener(bc);

        
        Link link = new Link("Send Message", new ExternalResource(
                "javascript:com.example.foo.myfunc()"));
        
        layout.addComponent(link);
        
        setJavaScriptFunction();
    }
    private void setButtonClickListener() {
        bc = new Button.ClickListener() {
			
        	@Override
			public void buttonClick(ClickEvent event) {
        		Notification.show("Start Downloading");
            	layout.addComponent(new Label("Thank you for clicking"));
                
                if(downloadInvisibleButton != null) {
                	layout.removeComponent(downloadInvisibleButton);
                }
                downloadInvisibleButton = new Button();
                downloadInvisibleButton.setId("DownloadButtonId");
//                downloadInvisibleButton.addStyleName("InvisibleButton");
                layout.addComponent(downloadInvisibleButton);
                
               
                String filePath = "/Users/jackho/project/1-1.jpg";
                File downloadFile = new File(filePath);
                FileResource res = new FileResource(downloadFile);
                
                FileDownloader fileDownloader = new FileDownloader(res);
                fileDownloader.extend(downloadInvisibleButton);
                //Simulate the click on downloadInvisibleButton by JavaScript
                Page.getCurrent().getJavaScript()
                   .execute("document.getElementById('DownloadButtonId').click();"
                   		+ "setTimeout(function() {"
                   		+ "com.example.foo.myfunc();}, 3000);");
                
                
                layout.addComponent(new Label("Thank you for downloading"));
			}
		};
    }
    public void enableButton() {
//    	button.removeStyleName("v-disabled");
//		
    	button.setEnabled(true);
		System.out.println("enableButton");
    }
    private void setJavaScriptFunction() {
        JavaScript.getCurrent().addFunction("com.example.foo.myfunc",
                new JavaScriptFunction() {
		@Override
		public void call(JsonArray arguments) {
				Notification.show("Received call");
//				button.addStyleName("v-disabled");
//		    	button.removeClickListener(bc);
				enableButton();
			}
		});
    }
    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
