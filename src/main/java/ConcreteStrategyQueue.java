import java.util.List;

public class ConcreteStrategyQueue implements Strategy {

    @Override
    public void addTask(List<Server> servers, Task t) {
        int min = 10000;
        for (Server s : servers) {
            if (s.getTasks().size() < min) {
                min = s.getTasks().size();
            }
        }
        for (Server s : servers) {
            if (s.getTasks().size() == min) {
                s.addTask(t);
                //System.out.println("S-a adaugat task-ul " + t.getID()+ "la coada " + s.getID());
                break;
            }
        }

    }

}
