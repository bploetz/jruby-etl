# Translates model.User Java objects to User Ruby objects
class UserTranslator
  def translate(java_user)
    ruby_user = User.where(:rdbms_id => java_user.getId).first
    ruby_user ||= User.new
    ruby_user.rdbms_id = java_user.getId
    ruby_user.username = java_user.getUsername
    ruby_user.save!
    ruby_user
  end
end
