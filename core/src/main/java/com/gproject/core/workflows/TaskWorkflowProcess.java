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
                "process.label" + "=Task Workflow Process",
                Constants.SERVICE_DESCRIPTION + "=Custom task workflow process."
        }
)
public class TaskWorkflowProcess implements WorkflowProcess {
    private static final Logger LOG = LoggerFactory.getLogger(TaskWorkflowProcess.class);
    private static final String TO_MOVE = "Vika Stay Strong!";
    private static final String PATH = "pathToMove";

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap processArguments) {
        LOG.info("\n ====================================Custom Workflow Process========================================");
        try {
            WorkflowData workflowData = workItem.getWorkflowData();
            if (workflowData.getPayloadType().equals("JCR_PATH")) {
                Session session = workflowSession.adaptTo(Session.class);
                String path = workflowData.getPayload().toString() + "/jcr:content";
                Node node = (Node) session.getItem(path);

                if(workflowData.getMetaDataMap().containsKey(PATH)) {
                    String processArgs = processArguments.get("PROCESS_ARGS", "string").toString();
                    node.setProperty(TO_MOVE, processArgs);

                    session.save();
                }


                  //   workItem .getWorkflowData().getPayload();
            }
        } catch (Exception e) {
            LOG.info("\n ERROR {} ", e.getMessage());
        }
    }
}
