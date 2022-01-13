/*

TEST 1:
4
2
60
2,30
2,4

TEST 2:
50
5
60
2,40
1,7

TEST 3:
1000
20
200
10,100
3,9

 */

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SimulationManager implements Runnable {
    public int timeLimit = 100; // max processing time
    public int numberOfServices = 3;
    public int numberOfClients = 100;
    public SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_QUEUE;
    private Scheduler scheduler;
    private List<Task> generatedTasks = new ArrayList();

    public SimulationManager(int N, int Q, int simulationTime, int tMinArrival, int tMaxArrival, int tMinService, int tMaxService) throws Exception {
        timeLimit = simulationTime;
        numberOfServices = Q;
        numberOfClients = N;
        scheduler = new Scheduler(numberOfServices, numberOfClients, selectionPolicy);
        generateNRandomTasks(numberOfClients, tMinArrival, tMaxArrival, tMinService, tMaxService);
    }

    public void generateNRandomTasks(int N, int tMinArrival, int tMaxArrival, int tMinService, int tMaxService) throws Exception {
        for (int i = 0; i < N; i++) {
            Task t = new Task();
            t.generateOneTask(i + 1, tMinArrival, tMaxArrival, tMinService, tMaxService);
            generatedTasks.add(t);
        }

    }

    @Override
    public void run() {
        int currentTime = 0;
        int closedCounter = 0;
        int max = -1;
        int maxCnt = 0;
        try {
            FileWriter file = new FileWriter("E:\\PROIECTE\\INTELIJ PROJECTS\\PT2021_30223_Varga_Alexandra_Ioana_Assignment_2\\src\\out.txt");

            while (currentTime <= timeLimit) {
                //System.out.println("Time:" + currentTime);
                file.write("Time:" + currentTime + "\n");
                Iterator itr = generatedTasks.iterator();
                while (itr.hasNext()) {
                    Task task = (Task) itr.next();
                    if (task.getArrTime() == currentTime) {
                        scheduler.dispatchTask(task);
                        itr.remove();
                    }
                }

                //System.out.print("Wainting clients: ");
                file.write("Wainting clients: ");
                for (Task task : generatedTasks) {
                    //System.out.print("(" + task.getID() + "," + task.getArrTime() + "," + task.getProcTime() + ")");
                    file.write("(" + task.getID() + "," + task.getArrTime() + "," + task.getProcTime() + ")");
                }
                //System.out.println();
                file.write("\n");
                for (Server s : scheduler.getServers()) {
                    //System.out.print("Coada " + s.getID() + ": ");
                    file.write("Queue " + s.getID() + ": ");
                    if (s.getTasks().isEmpty()) {
                        //System.out.println("closed");
                        file.write("closed" + "\n");
                    } else {
                        maxCnt = 0;
                        for (Task m : s.getTasks()) {
                            //System.out.print("(" + m.getID() + "," + m.getArrTime() + "," + m.getProcTime() + ")	");
                            file.write("(" + m.getID() + "," + m.getArrTime() + "," + m.getProcTime() + ")	");
                            maxCnt++;
                        }
                        if (max < maxCnt) {
                            max = maxCnt;
                        }
                        //System.out.println();
                        file.write("\n");
                    }
                }
                //System.out.println();
                file.write("\n");
                currentTime++;

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for (Server s : scheduler.getServers()) {
                    if (s.getTasks().isEmpty()) {
                        closedCounter++;
                    }
                }

                if ((scheduler.getServers().size() == closedCounter) && (generatedTasks.isEmpty())) {
                    //System.out.println("Time:" + currentTime);
                    //System.out.println("Waiting clients:");
                    file.write("Time:" + currentTime + "\n");
                    file.write("Waiting clients:" + "\n");
                    for (Server s : scheduler.getServers()) {
                        //System.out.println("Coada " + s.getID() + ": closed");
                        file.write("Queue " + s.getID() + ": closed" + "\n");
                    }
                    break;
                }
                closedCounter = 0;

            }
            scheduler.service = scheduler.service / numberOfClients;
            scheduler.waiting = scheduler.waiting / numberOfClients;

            file.write("\nEND OF SIMULATION\n");
            file.write("\nAverage Waiting Time: " + scheduler.waiting);
            ///MODIFICARE
            file.write("\nAverage Service Time: " + scheduler.service);
            file.write("\nPeak Hour: " + max);
            file.close();
            System.exit(0);

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {

        File file = new File("E:\\PROIECTE\\INTELIJ PROJECTS\\PT2021_30223_Varga_Alexandra_Ioana_Assignment_2\\src\\in.txt");
        BufferedReader bufferReader = new BufferedReader(new FileReader(file));
        String[] date = new String[5];

        /// citim din buffer datele
        for (int i = 0; i < 5; i++) {
            date[i] = bufferReader.readLine();
        }

        /// transformam nr de clienti, nr de cozi si simulation time in Integer
        int N = Integer.parseInt(date[0]);
        int Q = Integer.parseInt(date[1]);
        int simTime = Integer.parseInt(date[2]);

        String[] arrTime = date[3].split(",");
        String[] serviceTime = date[4].split(",");

        int tMinArr = Integer.parseInt(arrTime[0]);
        int tMaxArr = Integer.parseInt(arrTime[1]);
        int tMinSer = Integer.parseInt(serviceTime[0]);
        int tMaxSer = Integer.parseInt(serviceTime[1]);

        ///apelez managerul de simulare pentru a executa programul
        SimulationManager gen = new SimulationManager(N, Q, simTime, tMinArr, tMaxArr, tMinSer, tMaxSer);
        Thread t = new Thread(gen);
        t.start();
    }
}
