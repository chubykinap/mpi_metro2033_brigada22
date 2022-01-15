$(document).ready(function() {
    $(function(){

        $("#change").click(function(){

          check = false;

          $('#ajax_change_post *').filter(':input').each(function(){
              if($(this).val() == ''){
                check = true;
              }
          });

          if (check == true){
            alert("Не все данные заполнены!");
            return;
          }

          var add_soldier_to_post = {
            "post_id":$("#post_id").val(),
            "soldier_id":$("#soldier").val()
          };

          $.ajax({
            type: "POST",
            url: "/posts/add_soldier_to_post",
            contentType: "application/json",
            data: JSON.stringify(add_soldier_to_post),
            // dataType: "dataType",
            success: function (data) {
              console.log("success");
              console.log(data);
              window.location.href = '/posts';
            }
          });
          console.log(add_soldier_to_post);
        });
    })
});