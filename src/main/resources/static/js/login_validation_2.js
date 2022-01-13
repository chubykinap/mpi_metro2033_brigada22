$(document).ready(function() {
    $( "#username" )
      .focusout(function() {

       event.preventDefault();

       alert("hello");

        var check_login = {
          "username":$("#username").val()
        };

        $.ajax({
              type: 'GET',
              url: 'http://localhost:8080/validation/test',
              //data: new FormData(this),
              //dataType: 'json',
              contentType: false,
              processData:false,//this is a must
              success: function(response){
                    console.log(response);
              }
        });

//        $.ajax({
//            type: "GET",
//            contentType: "application/json",
//            url: "/validation",
//            //data: JSON.stringify(check_login),
//            dataType: 'application/json',
//            //cache: false,
//            //timeout: 600000,
//            success: function (data) {
//
////                var json = "<h4>Ajax Response</h4><pre>"
////                    + JSON.stringify(data, null, 4) + "</pre>";
////                $('#username_validation_warning').html(json);
//
//                console.log(data);
//
//            },
//            error: function (e) {
////
////                var json = "<h4>Ajax Response Error</h4><pre>"
////                    + e.responseText + "</pre>";
////                 $('#username_validation_warning').html(json);
//                console.log(e);
//            }
//        });
    });
});

//        $.ajax({
//            type: "POST",
//            url: "/validation",
//            contentType: "application/json",
//            data: JSON.stringify(check_login),
//            //dataType: "json",
//            success: function (response) {
//              console.log("success");
//              console.log(JSON.stringify(response));
//
//              if (!response.data){
//                $( "#username_validation_warning" ).text("Не правильно введен логин");
//              }
//              else{
//                $( "#username_validation_warning" ).text("");
//              }
//            }
//        });
//      });