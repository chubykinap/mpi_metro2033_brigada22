$(function(){

    $(document).on('click', '#delete', function(event)
    {
        event.preventDefault();
        if (window.confirm("Удалить военного?")){
            var tr = $(this).closest('tr');
            var soldier_id = $(this).closest('a').attr("value");
            if (soldier_id == '') {
              alert("Военный не найден!");
              return;
            }

            var soldier = {
                "soldier_id": soldier_id
            };

            $.ajax({
                type: "POST",
                url: "/army/delete",
                contentType: "application/json",
                data: JSON.stringify(soldier),
                success: function (data) {
                  console.log("success");
                  if (data.status == "Error"){
                    alert(data.data);
                    return;
                  }
                  else{
                    show_notification(data.data);
                    tr.remove();
                  }
                },
                error: function (e){
                    console.log(e);
                }
            });
        }
     });

      function show_notification(info){
         $('#notification').text("Солдат " + info.login + " " + info.name + " " + info.surname + " " + info.rank + " удален");
         $('#notification').show();
         $('#notification').delay(7000).hide(0);
      }

})