import React, {useState} from 'react';
import axios from 'axios';
import useAsync from './useAsync';
import FileInfo from "./FileInfo";


async function getDirs() {
    const response = await axios.get(
        '/api/dirs'
    );
    return response.data;
}

function Dirs() {
    const [file, setFile] = useState(null);
    const [index, setIndex] = useState(0);
    const [state, refetch] = useAsync(getDirs, [], true);

    const { loading, data: dirs, error } = state; // state.data 를 users 키워드로 조회

    if (loading) return <div>로딩중..</div>;
    if (error) return <div>에러가 발생했습니다</div>;
    if (!dirs) return <button onClick={refetch}>불러오기</button>;


    const back = () => {
        if(index > 0 ){
            setIndex(index - 1);
            setFile(dirs[index].name);
        }
    }
    const next = () =>{
        if(index < dirs.length - 1){
            setFile(dirs[index].name);
            setIndex(index + 1);
        }else{
            setIndex(0);
        }
    }

    const naver = () =>{
        window.open("https://finance.naver.com/item/fchart.naver?code="+file, 'Naver', '');
    }

    return (
        <>
            <table>
                <tr>
                    <td>
                        <div style={{overflow:"scroll", width:"120px", height:"400px"}}>
                            {dirs.map(dir => (
                                <div>
                                    <input type='checkbox'/>
                                    <a onClick={() => setFile(dir.name)} style={{ cursor: 'pointer' }}>{dir.name}</a>
                                </div>
                            ))}
                        </div>
                    </td>
                    <td>
                        <div>
                            {file && <FileInfo name={file}/>}
                        </div>
                        <div>
                            <button onClick={back}>뒤로</button>
                            <button onClick={next}>다음</button>
                            <button onClick={naver}>네이버차트 보기</button>
                        </div>
                    </td>
                </tr>
            </table>

            {/*<button onClick={refetch}>다시 불러오기</button>*/}

        </>
    );
}

export default Dirs;