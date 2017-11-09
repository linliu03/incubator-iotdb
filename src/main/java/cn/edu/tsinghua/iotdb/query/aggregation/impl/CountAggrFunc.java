package cn.edu.tsinghua.iotdb.query.aggregation.impl;

import java.io.IOException;
import java.util.List;

import cn.edu.tsinghua.iotdb.query.aggregation.AggregateFunction;
import cn.edu.tsinghua.iotdb.query.aggregation.AggregationConstant;
import cn.edu.tsinghua.iotdb.query.dataset.InsertDynamicData;
import cn.edu.tsinghua.tsfile.common.exception.ProcessorException;
import cn.edu.tsinghua.tsfile.file.metadata.enums.TSDataType;
import cn.edu.tsinghua.tsfile.format.PageHeader;
import cn.edu.tsinghua.tsfile.timeseries.read.query.DynamicOneColumnData;

public class CountAggrFunc extends AggregateFunction {

    public CountAggrFunc() {
        super(AggregationConstant.COUNT, TSDataType.INT64);
        result.data.putTime(0);
        result.data.putLong(0);
    }

    @Override
    public void calculateValueFromPageHeader(PageHeader pageHeader) {
        long preValue = result.data.getLong(0);
        preValue += pageHeader.data_page_header.num_rows;
        result.data.setLong(0, preValue);
    }

    @Override
    public void calculateValueFromDataPage(DynamicOneColumnData dataInThisPage) throws IOException, ProcessorException {
        long preValue = result.data.getLong(0);
        preValue += dataInThisPage.valueLength;
        result.data.setLong(0, preValue);
    }

    @Override
    public int calculateValueFromDataPage(DynamicOneColumnData dataInThisPage, List<Long> timestamps, int timeIndex) {
        return 0;
    }

    @Override
    public void calculateValueFromLeftMemoryData(InsertDynamicData insertMemoryData) throws IOException, ProcessorException {
        long preValue = result.data.getLong(0);
        Object count = insertMemoryData.calcAggregation(AggregationConstant.COUNT);
        preValue += (long) count;
        result.data.setLong(0, preValue);
    }

    @Override
    public boolean calcAggregationUsingTimestamps(InsertDynamicData insertMemoryData, List<Long> timestamps, int timeIndex) throws IOException, ProcessorException {
        while (timeIndex < timestamps.size()) {
           if (insertMemoryData.hasInsertData()) {
               if (timestamps.get(timeIndex) == insertMemoryData.getCurrentMinTime()) {
                   long preValue = result.data.getLong(0);
                   preValue += 1;
                   result.data.setLong(0, preValue);
                   timeIndex ++;
                   insertMemoryData.removeCurrentValue();
               } else if (timestamps.get(timeIndex) > insertMemoryData.getCurrentMinTime()) {
                   insertMemoryData.removeCurrentValue();
               } else {
                   timeIndex += 1;
               }
           } else {
               break;
           }
        }

        return insertMemoryData.hasInsertData();
    }
}