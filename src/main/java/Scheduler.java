import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Server> servers = new ArrayList();
    private int maxNoServers;
    private int maxTasksPerServer;
    public float service = 0;
    public float waiting = 0;
    private Strategy strategy;

    public Scheduler(int maxNoServers, int maxTasksServer, SelectionPolicy policy) {
        this.maxNoServers = maxNoServers;
        this.maxTasksPerServer = maxTasksServer;

        for (int i = 0; i < maxNoServers; i++) {
            Server server = new Server();
            server.setID(i);
            Thread t = new Thread(server);
            servers.add(server);
            t.start();
        }
        this.selectStrategy(policy);
    }

    public void selectStrategy(SelectionPolicy policy) {
        if (policy == SelectionPolicy.SHORTEST_QUEUE) {
            strategy = new ConcreteStrategyQueue();
        }
        if (policy == SelectionPolicy.SHORTEST_TIME) {
            strategy = new ConcreteStrategyTime();
        }
    }

    public void dispatchTask(Task t) {
        strategy.addTask(servers, t);
        service += t.getProcTime();
        waiting += t.getArrTime();
    }

    public List<Server> getServers() {
        return servers;
    }

}
