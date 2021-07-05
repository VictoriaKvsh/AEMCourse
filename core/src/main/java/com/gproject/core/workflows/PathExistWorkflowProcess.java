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
import javax.jcr.Session;

@Component(
        immediate = true,
        property = {
                "process.label" + "=Path Exist Workflow Process",
                Constants.SERVICE_DESCRIPTION + "=Custom task workflow process."
        }
)
public class PathExistWorkflowProcess implements WorkflowProcess {
    private static final Logger LOG = LoggerFactory.getLogger(PathExistWorkflowProcess.class);
    public static final String PATH_TO_MOVE_PROPERTY = "pathToMove";
    public static final String PATH_PROPERTY = "path";
    public static final String PATH_PROPERTY_DEFINED = "defined";
    public static final String PATH_PROPERTY_NOT_DEFINED = "notDefined";
    public static final String JCR_PATH = "JCR_PATH";
    public static final String JCR_CONTENT = "/jcr:content";

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap processArguments) {
        LOG.info("\n ====================================Custom Workflow Process========================================");
        try {
            WorkflowData workflowData = workItem.getWorkflowData();
            if (workflowData.getPayloadType().equals(JCR_PATH)) {
                Session session = workflowSession.adaptTo(Session.class);
                String path = workflowData.getPayload().toString() + JCR_CONTENT;
                Node node = (Node) session.getItem(path);
                if (node.hasProperty(PATH_TO_MOVE_PROPERTY)) {
                    workItem.getWorkflowData().getMetaDataMap().put(PATH_PROPERTY, PATH_PROPERTY_DEFINED);
                } else {
                    workItem.getWorkflowData().getMetaDataMap().put(PATH_PROPERTY, PATH_PROPERTY_NOT_DEFINED);
                }
            }
        } catch (Exception e) {
            LOG.info("\n ERROR {} ", e.getMessage());
        }
    }


}
