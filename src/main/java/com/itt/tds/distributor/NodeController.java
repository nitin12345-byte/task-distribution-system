package com.itt.tds.distributor;

import com.itt.tds.core.enums.NodeStatus;
import com.itt.tds.core.Constants;
import com.itt.tds.core.Networking.TDSRequest;
import com.itt.tds.core.Networking.TDSResponse;
import com.itt.tds.core.enums.ResponseStatus;
import com.itt.tds.core.enums.TDSResponseErrorCode;
import com.itt.tds.core.logging.LogManager;
import com.itt.tds.core.logging.Logger;
import com.itt.tds.core.model.Capability;
import com.itt.tds.core.model.Node;
import com.itt.tds.distributor.db.DBManager;
import com.itt.tds.distributor.db.dao.CapabilityDAO;
import com.itt.tds.distributor.db.dao.NodeDAO;
import com.itt.tds.distributor.db.exceptions.DBException;
import com.itt.tds.distributor.db.exceptions.RecordAlreadyExistException;
import java.sql.Connection;

public class NodeController implements TDSController {

    private Connection dbConnection;
    private Logger logger;

    public NodeController() {
        logger = LogManager.getLogger(this.getClass().getName());
    }

    @Override
    public TDSResponse processRequest(TDSRequest tdsRequest) {

        logger.logInfo("processrequest", "NodeController is processing the request");
        String method = tdsRequest.getMethod();
        dbConnection = DBManager.getInstance().createConnection();

        switch (method) {

            case Constants.NODE_REGISTER -> {
                return registerNode(tdsRequest);
            }

            case Constants.NODE_UNREGISTER -> {
                return unregisterNode(tdsRequest);
            }

            case Constants.NODE_HEARTBEAT -> {
                return makeNodeAvailable(tdsRequest);
            }

            case Constants.NODE_ADD_CAPABILITY -> {
                return addCapability(tdsRequest);
            }

            case Constants.NODE_REMOVE_CAPABILITY -> {
                return removeCapability(tdsRequest);
            }

            default -> {
                return new TDSResponse();
            }
        }
    }

    private TDSResponse registerNode(TDSRequest tdsRequest) {
        logger.logInfo("registerNode", "Controller is regestring the node");
        TDSResponse tdsResponse = new TDSResponse();
        Node node = new Node();
        node.setIpAddress((String) tdsRequest.getParameter(Constants.IP_ADDRESS));
        node.setHostName((String) tdsRequest.getParameter(Constants.HOST_NAME));
        node.setPortNumber(Double.valueOf((double) tdsRequest.getParameter(Constants.PORT_NUMBER)).intValue());
        node.setStatus((String) tdsRequest.getParameter(Constants.STATUS));
        NodeDAO nodeDAO = new NodeDAO(dbConnection);
        try {
            String id = nodeDAO.save(node);
            tdsResponse.setStatus(ResponseStatus.OK.getValue());
            tdsResponse.setValue(Constants.NODE_ID, id);
            logger.logInfo("registerNode", "Node registered successfully");
        } catch (DBException exception) {
            tdsResponse.setStatus(ResponseStatus.FAIL.getValue());
            tdsResponse.setErrorMessage(exception.getMessage());
            tdsResponse.setErrorCode(TDSResponseErrorCode.DATABASE_ERROR.getValue());
            logger.logError("registerNode", "Database error occured", exception);

        }
        return tdsResponse;
    }

    private TDSResponse unregisterNode(TDSRequest tdsRequest) {
        TDSResponse tdsResponse = new TDSResponse();
        String nodeId = (String) tdsRequest.getParameter(Constants.NODE_ID);
        NodeDAO nodeDAO = new NodeDAO(dbConnection);
        try {
            nodeDAO.delete(nodeId);
            tdsResponse.setStatus(ResponseStatus.OK.getValue());
        } catch (DBException exception) {
            tdsResponse.setStatus(ResponseStatus.FAIL.getValue());
            tdsResponse.setErrorCode(TDSResponseErrorCode.DATABASE_ERROR.getValue());
            tdsResponse.setErrorMessage(exception.getMessage());
        }
        return tdsResponse;

    }

    private TDSResponse makeNodeAvailable(TDSRequest tdsRequest) {
        logger.logInfo("makeNodeAvailable", "Controller making node available");
        TDSResponse tdsResponse = new TDSResponse();
        String nodeId = (String) tdsRequest.getParameter(Constants.NODE_ID);
        String ipAddress = (String) tdsRequest.getParameter(Constants.IP_ADDRESS);
        NodeDAO nodeDAO = new NodeDAO(dbConnection);

        try {
            nodeDAO.updateIpAddress(nodeId, ipAddress);
            nodeDAO.updateStatus(nodeId, NodeStatus.AVAILABLE.name());
            tdsResponse.setStatus(ResponseStatus.OK.getValue());
            logger.logInfo("makeNodeAvailable", "Node is available");
        } catch (DBException exception) {
            tdsResponse.setStatus(ResponseStatus.FAIL.getValue());
            tdsResponse.setErrorCode(TDSResponseErrorCode.DATABASE_ERROR.getValue());
            tdsResponse.setErrorMessage(exception.getMessage());
            logger.logError("makeNodeAvailable", "Database error occured", exception);
        }
        return tdsResponse;
    }

    private TDSResponse addCapability(TDSRequest tdsRequest) {
        logger.logInfo("addCapability", "Capability is getting added");
        TDSResponse tdsResponse = new TDSResponse();
        String nodeId = (String) tdsRequest.getParameter(Constants.NODE_ID);
        String capabilityName = (String) tdsRequest.getParameter(Constants.CAPABILITY_NAME);
        Capability capability = new Capability();
        capability.setName(capabilityName);
        capability.setNodeId(nodeId);

        try {
            CapabilityDAO capabilityDAO = new CapabilityDAO(dbConnection);
            capabilityDAO.save(capability);
            tdsResponse.setStatus(ResponseStatus.OK.getValue());
            logger.logInfo("addCapability", "Capability added successfully");

        } catch (RecordAlreadyExistException exception) {
            tdsResponse.setStatus(ResponseStatus.FAIL.getValue());
            tdsResponse.setErrorCode(TDSResponseErrorCode.RECORD_ALREADY_EXIST.getValue());
            tdsResponse.setErrorMessage(exception.getMessage());
        } catch (DBException exception) {
            tdsResponse.setStatus(ResponseStatus.FAIL.getValue());
            tdsResponse.setErrorCode(TDSResponseErrorCode.DATABASE_ERROR.getValue());
            tdsResponse.setErrorMessage(exception.getMessage());
        }

        return tdsResponse;
    }

    private TDSResponse removeCapability(TDSRequest tdsRequest) {
        logger.logInfo("removeCapability", "Capability is getting removed");

        TDSResponse tdsResponse = new TDSResponse();
        String nodeId = (String) tdsRequest.getParameter(Constants.NODE_ID);
        String capabilityName = (String) tdsRequest.getParameter(Constants.CAPABILITY_NAME);
        Capability capability = new Capability();
        capability.setName(capabilityName);
        capability.setNodeId(nodeId);

        try {
            CapabilityDAO capabilityDAO = new CapabilityDAO(dbConnection);
            capabilityDAO.delete(capability);
            tdsResponse.setStatus(ResponseStatus.OK.getValue());
            logger.logInfo("addCapability", "Capability is removed successfuly");

        } catch (DBException exception) {
            tdsResponse.setStatus(ResponseStatus.FAIL.getValue());
            tdsResponse.setErrorCode(TDSResponseErrorCode.DATABASE_ERROR.getValue());
            tdsResponse.setErrorMessage(exception.getMessage());
        }

        return tdsResponse;
    }

}
