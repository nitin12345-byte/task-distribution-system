package com.itt.tds.client;

/**
 *
 * @author nitin.jangid
 */
public class HelpCommandExecutor implements CommandExecutor {

    @Override
    public void executeCommand(String[] parameter) {
        System.out.println("1. [task  file_name] : This command is use to send the task to the distributor ");
        System.out.println("2. [status task_id] : This command is use to get the status of the task");
        System.out.println("3. [result task_id] : This command is use to get result of the task");
        System.out.println("4. [register] : This command is use to register a client");
        System.out.println("5. [unregister] : This command is use to unregister a client");
        System.out.println("6. [tasks] : This command is use to get a list of all the tasks");
        System.out.println("7. [config ] : This command is use to get the configuration of the distributor");
        System.out.println("8. [config ip {ip_address}] : This command is use to set the IP address of the distributor");
    }
}
