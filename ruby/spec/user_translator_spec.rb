require 'spec_helper'

describe UserTranslator do
  before :each do
    @mock_java_user = double("User")
    @mock_java_user.stub(:getId) {12345}
    @mock_java_user.stub(:getUsername) {"joe"}
    @user_translator = UserTranslator.new
  end

  context "#translate" do
    it "should create users if they are not found" do
      @user_translator.translate(@mock_java_user)
      @user = User.where(:rdbms_id => @mock_java_user.getId).first
      @user.should_not be_nil
      @user.rdbms_id.should == 12345
      @user.username.should == "joe"
    end

    it "should update users if they are found" do
      @user_translator.translate(@mock_java_user)
      @mock_java_user.stub(:getUsername) {"sam"}
      @user_translator.translate(@mock_java_user)
      @user = User.where(:rdbms_id => @mock_java_user.getId).first
      @user.should_not be_nil
      @user.rdbms_id.should == 12345
      @user.username.should == "sam"
    end
  end
end
