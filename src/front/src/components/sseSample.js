import React, {useEffect, useState} from "react";


export default function ResultDisplay(){
    const [data, setData] = useState('');

    const fetchSSE = () => {
        const eventSource = new EventSource('/api/stockRecvService');

        eventSource.onopen = () => {
            // 연결 시 할 일
            setData("start");
        };

        eventSource.onmessage = async (e) => {
            const res = await e.data;
            const parsedData = JSON.parse(res);

            // 받아오는 data로 할 일
            console.log(JSON.stringify(parsedData));
            setData(JSON.stringify(parsedData));
        };

        eventSource.onerror = (e: any) => {
            // 종료 또는 에러 발생 시 할 일
            eventSource.close();

            if (e.error) {
                // 에러 발생 시 할 일
                setData(e.error);
            }

            if (e.target.readyState === EventSource.CLOSED) {
                // 종료 시 할 일
                setData("완료.............");
            }
        };
    };




    return (
      <div>
          {data}
          <button onClick={fetchSSE}>클릭</button>
      </div>
    );
}