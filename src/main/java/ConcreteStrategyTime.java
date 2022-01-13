import java.util.List;

public class ConcreteStrategyTime implements Strategy {

    @Override
    public void addTask(List<Server> servers, Task t) {
        int min = 1000;
        for (Server s : servers) {
            if (s.getwaitingPeriod() < min) {
                min = s.getwaitingPeriod();
            }
        }
        for (Server s : servers) {
            if (s.getwaitingPeriod() == min) {
                s.addTask(t);
                //System.out.println("S-a adaugat task-ul " + t.getID() + "la coada " + s.getID());
                break;
            }
        }
    }
}
