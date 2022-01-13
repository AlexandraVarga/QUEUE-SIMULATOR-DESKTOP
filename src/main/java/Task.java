import java.util.Random;

public class Task {
    private int ID;
    private int arrivalTime;
    private int processingTime;

    public void generateOneTask(int id, int tMinArr, int tMaxArr, int tMinSer, int tMaxSer) throws Exception {

        Random random = new Random();
        int randomArrival = random.nextInt((tMaxArr - tMinArr) + 1) + tMinArr;
        this.setArrTime(randomArrival);
        int randomService = random.nextInt((tMaxSer - tMinSer) + 1) + tMinSer;
        this.setProcTime(randomService);
        this.setID(id);

    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    void setArrTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    void setProcTime(int processingTime) {
        this.processingTime = processingTime;
    }

    int getArrTime() {
        return arrivalTime;
    }

    int getProcTime() {
        return processingTime;
    }

}
