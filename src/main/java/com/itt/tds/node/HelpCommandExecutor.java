package com.itt.tds.node;

/**
 *
 * @author nitin.jangid
 */
public class HelpCommandExecutor implements CommandExecutor {

    @Override
    public void executeCommand(String[] parameter) {
        System.out.println("1. [add-capability  capability_name] : This command is use to send the task to the distributor ");
        System.out.println("2. [remove-capability capability_name] : This command is use to get the status of the task");
        System.out.println("3. [register] : This command is use to register a node");
        System.out.println("4. [start ] : This command use to start the node to accept the request from the distributor");
        System.out.println("5. [config ] : This command is use to get the configuration of the distributor");
        System.out.println("6. [config ip {ip_address}] : This command is use to set the IP address of the distributor");
    }
}
