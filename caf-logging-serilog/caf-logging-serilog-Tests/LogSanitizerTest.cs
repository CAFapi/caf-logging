using Microsoft.VisualStudio.TestTools.UnitTesting;
using nuget_sample_caf_logging;

namespace caf_logging_serilog_Tests
{
    [TestClass]
    public class LogSanitizerTest
    {
        
        [TestMethod]
        public void SanitizeMessage()
        {
            StringAssert.Equals("test", LogSanitizer.SanitizeMessage("test[^-]"));
        }
        
        [TestMethod]
        public void IsMessageSafeToLog()
        {
            Assert.IsTrue(LogSanitizer.IsMessageSafeToLog("test"));
            Assert.IsFalse(LogSanitizer.IsMessageSafeToLog("{test"));
        } 



    }
}