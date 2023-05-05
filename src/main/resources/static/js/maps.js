 // console.log(window.navigator.geolocation.watchPosition(success));

 function on() {
    //내위치
    // window.navigator.geolocation.watchPosition()

    $.ajax({
      url: "http://apis.data.go.kr/6260000/AttractionService/getAttractionKr?ServiceKey=DN2KInDxnssVVfjbPElgQOIERFqiTKpyKAaZ0lD%2BvpYonX%2BuhCbHcx3e4Aof0mZU5GssErjqcXeYfE18Wrc1tA%3D%3D&numOfRows=10&pageNo=1&resultType=json",
      dataType: "json",

      success: function (data) {
        // let myData = data["getAttractionKr"].item[1].MAIN_IMG_NORMAL;
        // console.log(myData);
        //map(function(element{return element.firstName}) )
        const locations = data["getAttractionKr"].item.map((spot) => [
          //element안에 위도,경로 오브젝트를 for문
          //ex  = 0:['흰여울문화마을', 35.07885, 129.04402]
          spot.PLACE,
          spot.LAT,
          spot.LNG,
        ]);

        console.log("locations", locations);
        //google.maps api함수안에 배열을 넣어줌
        drqwMap(locations);
      },
    });
  }

  //"LAT":35.07885,"LNG":129.04402 PLACE":"흰여울문화마을"

  function drqwMap(locations) {
    // 매개변수의 형태
    // locations = [["지역이름",위도,경도]
    //               , ["지역이름",위도,경도]]

    // 맵 생성
    const map = new google.maps.Map(document.getElementById("map"), {
      zoom: 13,
      center: new google.maps.LatLng(locations[0][1], locations[0][2]),
      mapTypeId: google.maps.MapTypeId.ROADMAP,
    });

    const infowindow = new google.maps.InfoWindow();

    let marker, i;

    //로케이션 별로 마크 생성
    for (i = 0; i < locations.length; i++) {
      marker = new google.maps.Marker({
        position: new google.maps.LatLng(locations[i][1], locations[i][2]),
        map: map,
      });

      // 마크를 글릭햇을 때 보여주는 경로
      google.maps.event.addListener(
        marker,
        "click",
        (function (marker, i) {
          return function () {
            infowindow.setContent(locations[i][0]);
            infowindow.open(map, marker);
          };
        })(marker, i)
      );
    }
    //////////////내위치
    const locationButton = document.createElement("button");

    locationButton.textContent = "Pan to Current Location";
    locationButton.classList.add("custom-map-control-button");
    map.controls[google.maps.ControlPosition.TOP_CENTER].push(
      locationButton
    );
    locationButton.addEventListener("click", () => {
      // Try HTML5 geolocation.
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
          (position) => {
            const pos = {
              lat: position.coords.latitude,
              lng: position.coords.longitude,
            };
            console.log(pos);
            infowindow.setPosition(pos);
            infowindow.setContent("내위치");
            infowindow.open(map);
            map.setCenter(pos);
          },
          () => {
            handleLocationError(true, infowindow, map.getCenter());
          }
        );
      } else {
        // Browser doesn't support Geolocation
        handleLocationError(false, infowindow, map.getCenter());
      }
    });

    function handleLocationError(browserHasGeolocation, infowindow, pos) {
      infowindow.setPosition(pos);
      infowindow.setContent(
        browserHasGeolocation
          ? "Error: The Geolocation service failed."
          : "Error: Your browser doesn't support geolocation."
      );
      infowindow.open(map);
    }
  }