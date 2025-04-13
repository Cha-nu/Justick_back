import {useEffect, useState} from "react";
import axios from "axios";

function App() {
    const [data, setData] = useState([]);

    useEffect(() => {
        axios.get('http://justick-env.eba-bahjbyqe.ap-northeast-2.elasticbeanstalk.com/api/cabbage/HighPredict')
            .then((res) => {
                setData(res.data);
            })
    }, []);
    return (
        <div className="App">
            {data.map((item) => (
                <div key={item.id}>
                    <p>이름: {item.name}</p>
                    <p>카테고리: {item.category}</p>
                    <p>등급: {item.grade}</p>
                    <p>예측 가격: {item.predictPrice}</p>
                </div>
            ))}
        </div>
    );
}

export default App;