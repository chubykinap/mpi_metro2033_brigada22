$(document).ready(function() {

    $(document).on('click', '#button', function(e) {
       e.preventDefault();
      var check = false;

      var name = $("#name").val();
      if (name == '') {
          $( "#name_validation" ).text("Введите название датчика");
          check = true;
      }

      if (validate_name()){
        check = true;
      }

      var location = $("#location").val();
      if (location == '') {
          $( "#location_validation" ).text("Введите местоположение датчика");
          check = true;
      }

      if (validate_location()){
        check = true;
      }

      var post_id = $("#post").val();
      if (post_id == '') {
          $( "#post_validation" ).text("Введи пост датчика");
          check = true;
      }else{
          $( "#post_validation" ).text("");
      }

      if (validate_post()) {
          check = true;
      }

      if (check == false){
         var sensor = {
                     "name": name,
                     "location" : location,
                     "post_id" : post_id
                 };

         $.ajax({
             type: "POST",
             url: "/sensors/create",
             contentType: "application/json",
             data: JSON.stringify(sensor),
             success: function (data) {
               console.log("success");
               if (data.status == "Error"){
                 alert(data.data);
                 return;
               }
               else{
                 show_notification(data.data);
                 fromReset();
               }
             },
             error: function (e){
                 console.log(e);
             }
         });
      }else{
        alert("Введите правильно данные");
      }
    });

     function show_notification(info){
//        $('#notification').text("Данные датчика " + info.name + " " + info.location + " изменены");
        $('#notification').text("Датчик:  " + info.name + " " + info.location + " создан");
        $('#notification').show();
        $('#notification').delay(7000).hide(0);
     }

      function fromReset() {
          document.getElementById("create").reset();
      }

    function validate_name(){
       var regExp = new RegExp("^(?=.{1,100}$)(?![_.])(?!.*[_.]{2})[a-zA-ZА-Яа-я0-9]+(?<![_.])$");
       var OK = regExp.exec($("#name").val());
       if(!OK){
         $( "#name_validation" ).text("Не правильно введено название. Оно должно содержать от 1 до 100 симолов. Досутпные символы: английсике и русские буквы, цифры");
          return true;
       }else{
          $( "#name_validation" ).text("");
          return false;
       }
    }

    function validate_location(){
       var regExp = new RegExp("^(?=.{1,100}$)(?![_.])(?!.*[_.]{2})[a-zA-ZА-Яа-я0-9]+(?<![_.])$");
       var OK = regExp.exec($("#location").val());
       if(!OK){
         $( "#location_validation" ).text("Не правильно введено местоположение. Оно должно содержать от 1 до 100 симолов. Досутпные символы: английсике и русские буквы, цифры");
         return true;
       }else{
          $( "#location_validation" ).text("");
          return false;
       }
    }

    function validate_post(){
        var post = $("#post").val();
        if (post == '') {
            $( "#post_validation" ).text("Выбирете роль пользователя");
            return true;
        }else{
            $( "#post_validation" ).text("");
            return false;
        }
    }

    $("#name").on('keyup', function(){
       event.preventDefault();
       validate_name();
    });

    $("#location").on('keyup', function(){
       event.preventDefault();
       validate_location();
    });

    $( "#post" )
      .change(function() {
       event.preventDefault();
       validate_post();
    });

});

