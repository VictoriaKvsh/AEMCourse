package com.gproject.core.workflows;

import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

@Component(
        immediate = true,
        property = {
                "process.label" + "=Check Path Workflow Process",
                Constants.SERVICE_DESCRIPTION + "=Custom task workflow process."
        }
)
public class CheckPathWorkflowProcess implements WorkflowProcess {
    private static final Logger LOG = LoggerFactory.getLogger(CheckPathWorkflowProcess.class);
    public static final String JCR_PATH = "JCR_PATH";
    public static final String JCR_CONTENT = "/jcr:content";

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap processArguments) {
        try {
            WorkflowData workflowData = workItem.getWorkflowData();
            if (workflowData.getPayloadType().equals(JCR_PATH)) {
                Session session = workflowSession.adaptTo(Session.class);
                String pageNodePath = workflowData.getPayload().toString();
                Node node = (Node) session.getItem(pageNodePath + JCR_CONTENT);
                if (node.hasProperty(PathExistWorkflowProcess.PATH_TO_MOVE_PROPERTY)) {
                    String pathToMoveProperty = node.getProperty(PathExistWorkflowProcess.PATH_TO_MOVE_PROPERTY).getString();
                    if (isValidPath(pathToMoveProperty, session) && !pathToMoveProperty.equals(pageNodePath)) {
                        String nodeName = "/"+node.getProperty("jcr:title").getString();
                        session.move(pageNodePath, pathToMoveProperty+nodeName);
                        session.save();
                    }
                }
            }
        } catch (Exception e) {
            LOG.info("\n ERROR {} ", e.getMessage());
        }
    }

    public static boolean isValidPath(String path, Session session) {
        try {
            return session.nodeExists(path);
        } catch (RepositoryException e) {
            e.printStackTrace();
            return false;
        }
    }
}
