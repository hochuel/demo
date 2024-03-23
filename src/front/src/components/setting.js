import React from "react";
import ProgressDisplay from "./progress.tsx";

export default function SettingStock(){


    const todayStock = () => {
        {ProgressDisplay("/api/stockRecvService")};
    }

    return (
        <div>
            오늘 주식 정보 가져오기 :<button onClick={todayStock}>시작</button>
        </div>
    )
}