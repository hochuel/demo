import React, { Component } from "react";
import ApexChart from "react-apexcharts";

function StockChart({stockData}){

    return (
        <div>
            <div id="chart">
                <ApexChart
                    options=
                    {
                        {
                            theme: {mode: "dark",},
                            title: {text: stockData.info.hts_kor_isnm+"("+stockData.info.stck_shrn_iscd+")",align: 'left'},
                            xaxis: {type: "datetime",labels: {datetimeFormatter: {day: 'MM-dd'}}},
                            yaxis: {tooltip: {enabled: true}}
                        }
                    }

                    series=
                    {
                        [
                            {
                                name: 'candle',
                                type: 'candlestick',
                                data:
                                    stockData?.data.map((price) => {
                                        return [
                                            Date.parse(price.stck_bsop_date),
                                            price.stck_oprc,
                                            price.stck_hgpr,
                                            price.stck_lwpr,
                                            price.stck_clpr,
                                        ];
                                    }),
                            },
                            {
                                name: 'line',
                                type: 'line',
                                color: 'red',
                                data:
                                    stockData?.data.map((price) => {
                                        return [
                                            Date.parse(price.stck_bsop_date),
                                            price.stck_oprc,
                                        ];
                                    }),
                            }
                        ]
                    }
                    type="candlestick"
                    height={550}
                    width={1000}/>
            </div>
        </div>
    );

}

export default StockChart;