INSERT INTO mvp_core.ref_origin(code, name) VALUES
  ('street_kid', 'Street Kid'),
  ('corpo', 'Corpo'),
  ('nomad', 'Nomad')
ON CONFLICT (code) DO NOTHING;

INSERT INTO mvp_core.ref_class(code, name) VALUES
  ('solo', 'Solo'),
  ('netrunner', 'Netrunner'),
  ('techie', 'Techie')
ON CONFLICT (code) DO NOTHING;

INSERT INTO mvp_core.ref_faction(code, name) VALUES
  ('arasaka', 'Arasaka'),
  ('militech', 'Militech'),
  ('valentinos', 'Valentinos'),
  ('maelstrom', 'Maelstrom'),
  ('ncpd', 'NCPD')
ON CONFLICT (code) DO NOTHING;

INSERT INTO mvp_core.world_district_state(code, unrest, modifiers) VALUES
  ('watson', 40, '{}'::jsonb),
  ('westbrook', 55, '{}'::jsonb),
  ('heywood', 35, '{}'::jsonb)
ON CONFLICT (code) DO NOTHING;

INSERT INTO mvp_core.crafting_blueprint(code, name, inputs, output) VALUES
  ('smart_scope_mk1', 'Smart Scope Mk1', '{"electronics":2,"glass":1}'::jsonb, '{"item":"smart_scope_mk1"}'::jsonb),
  ('ricochet_rounds', 'Ricochet Rounds', '{"chemicals":1,"metal":2}'::jsonb, '{"item":"ricochet_rounds"}'::jsonb),
  ('gyro_stabilizer', 'Gyro Stabilizer', '{"metal":3,"mechanics":1}'::jsonb, '{"item":"gyro_stabilizer"}'::jsonb)
ON CONFLICT (code) DO NOTHING;

INSERT INTO mvp_core."order"(title, owner_account_id, state, access, payload)
VALUES
  ('tutorial-protect-convoy', NULL, 'open', 'public', '{"type":"tutorial"}'::jsonb),
  ('tutorial-hack-network', NULL, 'open', 'public', '{"type":"tutorial"}'::jsonb),
  ('tutorial-supply-run', NULL, 'open', 'public', '{"type":"tutorial"}'::jsonb)
ON CONFLICT DO NOTHING;


