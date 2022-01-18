$(document).ready(function() {

    $(document).on('click', '#button', function(e) {
       e.preventDefault();
      var check = false;

      var name = $("#name").val();
      if (name == '') {
          $( "#name_validation" ).text("Введите название поста");
          check = true;
      }

      if (validate_name()){
        check = true;
      }

      var location = $("#location").val();
      if (location == '') {
          $( "#location_validation" ).text("Введите местоположение поста");
          check = true;
      }

      if (validate_location()){
        check = true;
      }

      if (check == false){
         var post = {
                     "name": name,
                     "location" : location
                 };

         $.ajax({
             type: "POST",
             url: "/posts/create",
             contentType: "application/json",
             data: JSON.stringify(post),
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
        $('#notification').text("Пост:  " + info.name + " " + info.location + " создан");
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

    $("#name").on('keyup', function(){
       event.preventDefault();
       validate_name();
    });

    $("#location").on('keyup', function(){
       event.preventDefault();
       validate_location();
    });

});

