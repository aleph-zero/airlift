package com.facebook.presto.aggregations;

import com.facebook.presto.Cursor;
import com.facebook.presto.Range;
import com.facebook.presto.Tuple;
import com.facebook.presto.TupleInfo;

import javax.inject.Provider;

public class SumAggregation
        implements AggregationFunction
{
    public static final Provider<AggregationFunction> PROVIDER = new Provider<AggregationFunction>()
    {
        @Override
        public SumAggregation get()
        {
            return new SumAggregation();
        }
    };
    private static final TupleInfo TUPLE_INFO = new TupleInfo(TupleInfo.Type.FIXED_INT_64);

    private long sum;

    @Override
    public TupleInfo getTupleInfo()
    {
        return TUPLE_INFO;
    }

    @Override
    public void add(Cursor cursor, Range relevantRange)
    {
//  todo: rle code
//        cursor.advanceToPosition(relevantRange.getStart());
//        do {
//            long endPosition = Math.min(cursor.getCurrentValueEndPosition(), relevantRange.getEnd());
//            long size = endPosition - cursor.getPosition() + 1;
//            sum += (cursor.getLong(0) * size);
//            if (!cursor.hasNextPosition()) {
//                break;
//            }
//            cursor.advanceNextPosition();
//        }  while (relevantRange.contains(cursor.getPosition()));

        // advance to start of range
        cursor.advanceToPosition(relevantRange.getStart());
        while (relevantRange.contains(cursor.getPosition())) {
            sum += cursor.getLong(0);
            if (!cursor.hasNextPosition()) {
                break;
            }
            cursor.advanceNextPosition();
        }
    }

    @Override
    public Tuple evaluate()
    {
//        System.out.println(count + ", " + sum);
        return getTupleInfo().builder()
                .append(sum)
                .build();
    }
}