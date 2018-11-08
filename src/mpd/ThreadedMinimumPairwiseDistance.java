package mpd;

public class ThreadedMinimumPairwiseDistance implements MinimumPairwiseDistance {
    private long globalResult = Integer.MAX_VALUE;

    @Override
    public long minimumPairwiseDistance(int[] values) {
        //Creates the array of threads
        Thread[] threads = new Thread[4];
        //Creates and starts the Threads in threads for each of the four runnables
        for(int i = 0; i < 4; i++){
            switch(i){
                case 0 :
                    threads[i] = new Thread(new LowerLeft(values));
                    break;
                case 1 :
                    threads[i] = new Thread(new LowerRight(values));
                    break;
                case 2 :
                    threads[i] = new Thread(new Center(values));
                    break;
                case 3 :
                    threads[i] = new Thread(new UpperRight(values));
                    break;
            }
            threads[i].start();
        }
        //Joins all the Threads in threads
        for(int i = 0; i < 4; i++){
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return this.globalResult;
    }

    public void updateGlobalResult(long localResult){
        //Updates the globalResult if it finds a value less than it
        //in one of the Threads.
        if(this.globalResult > localResult){
            this.globalResult = localResult;
        }
    }

    public class LowerLeft implements Runnable{
        private int[] values;
        public LowerLeft(int[] values){
            this.values = values;
        }
        public void run(){
            long result = Integer.MAX_VALUE;
            for (int i = 0; i < values.length/2; ++i) {
                for (int j = 0; j < i; ++j) {
                    // Gives us all the pairs (i, j) where 0 <= j < i < values.length/2
                    long diff = Math.abs(values[i] - values[j]);
                    if (diff < result) {
                        result = diff;
                    }
                }
            }
            updateGlobalResult(result);
        }
    }

    public class LowerRight implements Runnable{
        private int[] values;
        public LowerRight(int[] values){
            this.values = values;
        }

        public void run(){
            long result = Integer.MAX_VALUE;
            for (int i = values.length/2; i < values.length; ++i) {
                for (int j = 0; j < i - (values.length/2); ++j) {
                    // Gives us all the pairs (i, j) where values.length/2 <= j + values.length/2 < i < values.length
                    long diff = Math.abs(values[i] - values[j]);
                    if (diff < result) {
                        result = diff;
                    }
                }
            }
            updateGlobalResult(result);
        }
    }

    public class Center implements Runnable{
        private int[] values;
        public Center(int[] values){
            this.values = values;
        }

        public void run(){
            long result = Integer.MAX_VALUE;
            for (int i = values.length/2; i < values.length; ++i) {
                for (int j = i - (values.length/2); j < i; ++j) {
                    // Gives us all the pairs (i, j) where values.length/2 <= i <= j + values.length/2 < values.length
                    long diff = Math.abs(values[i] - values[j]);
                    if (diff < result) {
                        result = diff;
                    }
                }
            }
            updateGlobalResult(result);
        }
    }

    public class UpperRight implements Runnable{
        private int[] values;
        public UpperRight(int[] values){
            this.values = values;
        }

        public void run(){
            long result = Integer.MAX_VALUE;
            for (int i = values.length/2; i < values.length; ++i) {
                for (int j = values.length/2; j < i; ++j) {
                    // Gives us all the pairs (i, j) where values.length/2 <= j < i < values.length
                    long diff = Math.abs(values[i] - values[j]);
                    if (diff < result) {
                        result = diff;
                    }
                }
            }
            updateGlobalResult(result);
        }
    }

}
