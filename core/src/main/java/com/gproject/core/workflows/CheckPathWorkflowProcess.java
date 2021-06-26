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

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap processArguments) {
        try {
            WorkflowData workflowData = workItem.getWorkflowData();
            if (workflowData.getPayloadType().equals("JCR_PATH")) {
                Session session = workflowSession.adaptTo(Session.class);
                String pageNodePath = workflowData.getPayload().toString();
                Node node = (Node) session.getItem(pageNodePath + "/jcr:content");
                if (node.hasProperty(PathExistWorkflowProcess.PATH_TO_MOVE_PROPERTY)) {
                    String pathToMoveProperty = node.getProperty(PathExistWorkflowProcess.PATH_TO_MOVE_PROPERTY).getString();
                    if (isValidPath(pathToMoveProperty, session) && !pathToMoveProperty.equals(pageNodePath)) {
                        session.move(pageNodePath, pathToMoveProperty);
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
            session.getNode(path);
       //     session.itemExists()
       //     session.nodeExists();
        } catch (RepositoryException ex) {
            return false;
        }
        return true;
    }
}
