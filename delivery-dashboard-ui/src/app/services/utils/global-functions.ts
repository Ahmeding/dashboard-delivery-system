export function executeGlobalFunction(
  callback:(
  ...args: any[])=>void, // this the function to execute
  context: any, //the context
  ...args: any[] // Argument to pass to the function
  ): void{
    try{
      console.log("exxecut e the provide function...");
      callback.apply(context,args) // Execute the callback in the correct context
      console.log('Function executed successfully.');
    }catch (error){
      console.error('An error occurred while executing the function:', error);
    }
  }
