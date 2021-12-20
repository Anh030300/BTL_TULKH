package model;

import data.Data;

import java.util.*;

public class Taxi {
    public List<Integer> list ;
    public int journey;
    public int maxVolume;
    public List<Integer> visitedList;
    public static int getLowestDistancePoint(int currentPoint,Data data,Set<Integer> canVisitPoint)
    {
        int lowestDistancePoint = 0 ;
        int lowestDistance = Integer.MAX_VALUE;
        for(int i:canVisitPoint)
        {
            if(data.distance[currentPoint][i]<lowestDistance)
            {
                lowestDistance = data.distance[currentPoint][i];
                lowestDistancePoint = i;
            }
        }
        return lowestDistancePoint;
    }
    public void calculateDistance(Data data)
    {

        journey = 0;
        int currentPoint = 0;
        boolean isEmpty = true;
        int remainVolume = this.maxVolume;

        Set<Integer> canVisitPoint = new HashSet<>();
        int in = 0;
        while((in<list.size())||(canVisitPoint.size()!=0)) {

            int index = -1;
            if(in<list.size()) index = list.get(in);
            if((index <= data.N)&&(index>0))
            {
                if(isEmpty) canVisitPoint.add(index);
            }
            else if((data.N+1<=index)&&( index<= data.N+ data.M))
            {
                if(remainVolume>=data.volume[index- data.N-1]) canVisitPoint.add(index);
            }
            int nextPoint = getLowestDistancePoint(currentPoint,data,canVisitPoint);
            visitedList.add(nextPoint);
            if (((1<=nextPoint)&&( nextPoint<= data.N+ data.M)))
            {
                in++;
                journey += data.distance[currentPoint][nextPoint];
                currentPoint = nextPoint;
                canVisitPoint.remove(nextPoint);
                canVisitPoint.add(nextPoint+data.M+ data.N);
                if (nextPoint <= data.N)
                {
                    isEmpty = false;
                }
                else
                {
                    remainVolume -= data.volume[nextPoint-data.N-1];
                }
            }
            else
            {
                journey += data.distance[currentPoint][nextPoint];
                currentPoint = nextPoint;
                canVisitPoint.remove(nextPoint);
                if (nextPoint <= 2*data.N+data.M)
                {
                    isEmpty = true;
                }
                else
                {
                    remainVolume += data.volume[nextPoint- 2*data.N-data.M-1];
                }
            }
        }
        journey += data.distance[visitedList.get(visitedList.size()-1)][0];
    }
    public String toString() {
        StringBuilder buff = new StringBuilder("Taxi [");

        for (int i = 0; i < visitedList.size(); i++)
            buff.append(visitedList.get(i)).append(" ");

        return buff.append("]").toString();
    }

}
