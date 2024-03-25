import * as React from 'react';

import AlertDialog from "./ui/AlertDialog";
import ModalUnstyled from "./ui/Register";
function App() {

    //서버에서 데이터를 받아 온다.
    //데이터 확인에따라 세팅이 안되어 있다는 alert를 띄워준다.

    function AlertFunc(){
        alert("잘 전달 되나??");
    }

    return (
        <div>
            <AlertDialog title={"알림"} 
                         msg={"한국투자 증권 설정 파일이 존재 하지 않습니다."}
                         complateButton={"설정화면"}
                         closeButton={"닫기"}
                         AlertFunc={AlertFunc}/>
            <ModalUnstyled />
        </div>
    );
}
export default App;
