$(document).ready(function() {

        $("#create").submit(function(event) {
            event.preventDefault();
            check = false;

            check = validate_courier();
            console.log(check);

            check = validate_station();
            console.log(check);

            check = validate_date();
            console.log(check);

            check = validate_items();
            console.log(check);

            if (check == true){
                alert("Не все данные заполнены!");
                return;
            }
            else {
                $(this).unbind('submit').submit()
            }

            var order = {
                "courier_id":$("#courier").val(),
                "station":$("#station").val(),
                "direction":$("#direction").text(),
                "date":$('#date_select').val(),
                "items":getTableData()
            };

            $.ajax({
                type: "POST",
                url: "/delivery",
                contentType: "application/json",
                data: JSON.stringify(order),
                success: function (data) {
                  console.log("success");
                  console.log(data);
                  window.location.href = '/delivery';
                }
            });
        });

        $("courier").keyup(function(){
            event.preventDefault()
            validate_courier();
        });

        $("station").keyup(function(){
            event.preventDefault()
            validate_station();
        });

        $("date").keyup(function(){
            event.preventDefault()
            validate_date();
        });

        $("t_items").change(function(){
            event.preventDefault()
            validate_date();
        });

});

function getTableData() {
    var $t_data = [];
    $("#t_items tbody tr").each(function(i) {
        var $c_data = [];
        var $item_name = $(this).find('#t_name').text();
        var $item_quantity = $(this).find('#t_quantity').val();
        if ($item_quantity > 0) {
            $c_data.push($item_name);
            $c_data.push($item_quantity);
            $t_data.push($c_data);
        }
    });
    return $t_data;
}

function validate_courier(){
    var courier = $("#courier").val();
    if (courier == '') {
        $( "#courier_validation" ).text("Выберите курьера");
        return true;
    }else{
        $( "#courier_validation" ).text("");
        return false;
    }
}

function validate_station(){
    var station = $("#station").val();
    if (station == '') {
        $( "#station_validation" ).text("Выберите станцию");
        return true;
    }else{
        $( "#station_validation" ).text("");
        return false;
    }
}

function validate_date(){
    var date = $("#date_select").val();
    if (date == '') {
        $( "#date_validation" ).text("Выберите дату доставки");
        return true;
    }else{
        $( "#date_validation" ).text("");
        return false;
    }
}

function validate_items(){
    var items = getTableData();
    console.log(items);
    if (items.length < 1) {
        $( "#item_validation" ).text("Не добавлены предметы для заказа");
        return true;
    }else{
        $( "#item_validation" ).text("");
        return false;
    }
}
