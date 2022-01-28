package com.itt.tds.distributor;

import com.itt.tds.core.Networking.TDSRequest;
import com.itt.tds.core.Networking.TDSResponse;


public interface TDSController {

    public TDSResponse processRequest(TDSRequest tdsRequest);
    
}
