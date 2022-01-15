$(document).ready(function() {

    $("#create").submit(function(e) {
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
          $(this).unbind('submit').submit()
      }else{
        alert("Введите правильно данные");
      }

    });

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

