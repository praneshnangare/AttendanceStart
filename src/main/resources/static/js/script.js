console.log("This is a script file");
const toggleSidebar = () =>{
  if($(".sidebar").is(":visible")){

    $(".sidebar").css("display" , "none");
    $(".content").css("margin-left", "0%");
    console.log("closed");
  }
  else{
    $(".sidebar").css("display" , "block");
    $(".content").css("margin-left", "20%");

  }
};